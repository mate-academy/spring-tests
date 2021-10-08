package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
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
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        Role actual = roleDao.save(roleAdmin);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("ADMIN", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(roleAdmin);

        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent(), "ADMIN role should be at the database.");
        Assertions.assertEquals("ADMIN", actual.get().getRoleName().name());
    }
}
