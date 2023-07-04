package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @Test
    void saveRole_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        try {
            Optional<Role> actual = roleDao.getRoleByName("ROLE");
            Assertions.assertTrue(actual.isEmpty());
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: ROLE", e.getMessage());
            return;
        }

        Assertions.fail("Expected to receive DataProcessingException");
    }
}
