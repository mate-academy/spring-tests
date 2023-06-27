package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final long ID = 1L;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
        Assertions.assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_NotOk() {
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ID, actual.get().getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }
}
