package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.java.mate.academy.AbstractTest;

class RoleDaoImplTest extends AbstractTest {
    private static final String USER_ROLE = "USER";
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
        Role role = new Role();
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(USER_ROLE, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_DataProcessing() {
        try {
            roleDao.getRoleByName("NOT_A_ROLE");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: NOT_A_ROLE", e.getMessage());
            return;
        }
        Assertions.fail("Expected to throw DataProcessingException for role name" + "NOT_A_ROLE");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}
