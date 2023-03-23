package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void getRoleByName_ok() {
        Role expected = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                "Should return actual role" + expected.getRoleName());
    }

    @Test
    void getRoleByName_NoSuchElementException() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        String roleName = "ADMIN";
        Optional<Role> actual = roleDao.getRoleByName(roleName);
        try {
            actual.get().getRoleName();
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present",
                    e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}
