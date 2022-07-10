package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String INVALID_ROLE = "MANAGER";
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role role = new Role();
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), 1L);
    }

    @Test
    void getRoleByName_ok() {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(roleAdmin);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent(), "ADMIN role should be at the database.");
        Assertions.assertEquals("ADMIN", actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_nonExistentRole_notOk() {
        try {
            roleDao.getRoleByName(INVALID_ROLE);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: " + INVALID_ROLE, e.getMessage());
            return;
        }
        Assertions.fail("Expected to throw DataProcessingException for role name");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}
