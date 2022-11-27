package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(role.getId());
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(role.getRoleName().name(), actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notFound_notOk() {
        Assertions.assertTrue(roleDao.getRoleByName(role.getRoleName().name()).isEmpty());
    }

    @Test
    void getRoleByName_incorrectRoleName_notOk() {
        String incorrectRoleName = "KEKW";
        Assertions.assertEquals("Couldn't get role by role name: " + incorrectRoleName,
                Assertions.assertThrows(DataProcessingException.class,
                        () -> roleDao.getRoleByName(incorrectRoleName)).getMessage());
    }
}
