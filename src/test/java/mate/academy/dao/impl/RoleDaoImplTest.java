package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Role.RoleName ROLE_ADMIN = Role.RoleName.ADMIN;
    private Role roleAdmin;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleAdmin = new Role(ROLE_ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleAdmin);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE_ADMIN, actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(roleAdmin);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ROLE_ADMIN, actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NoSuchName() {
        roleDao.save(roleAdmin);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertFalse(actual.isPresent());
    }
}
