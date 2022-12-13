package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(role, actual);
    }

    @Test
    void saveRole_emptyRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistingRoleInDb_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_notExistingRoleName_Ok() {
        roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("MASTER"));
    }
}
