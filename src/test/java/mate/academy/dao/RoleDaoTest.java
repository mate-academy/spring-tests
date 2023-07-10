package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE = Role.RoleName.ADMIN;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = getRole();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_existedRole_Ok() {
        getRole();
        Optional<Role> actual = roleDao.getRoleByName(ROLE.name());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(Role.RoleName.valueOf(ROLE.name()), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_notExistedRole_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }

    private Role getRole() {
        Role role = new Role();
        role.setRoleName(ROLE);
        return roleDao.save(role);
    }
}
