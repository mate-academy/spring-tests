package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role(Role.RoleName.USER);
        Role expected = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(ROLE_USER);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                "Should return actual role" + expected.getRoleName());
    }

    @Test
    void getRoleByName_nonExistingRole_notOk() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        String roleName = ROLE_ADMIN;
        Optional<Role> actual = roleDao.getRoleByName(roleName);
        NoSuchElementException thrown
                = Assertions.assertThrows(NoSuchElementException.class, () -> {
                    actual.get().getRoleName();
                });
        Assertions.assertEquals("No value present", thrown.getMessage(),
                "Expected to receive NoSuchElementException with message"
                        + " \"No value present\", but was: " + thrown.getMessage()
                        + " with exception: " + thrown.getCause());
    }
}
