package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String USER = "USER";
    private static final String NOT_USER = "NOT_USER";
    private RoleDao roleDao;
    private Role userRole;
    private Role actual;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        actual = roleDao.save(userRole);
    }

    @Test
    void save_Ok() {
        assertNotNull(actual);
        assertEquals(USER, actual.getRoleName().name());
    }

    @Test
    void save_nullRole_notOk() {
        try {
            roleDao.save(null);
        } catch (DataProcessingException e) {
            assertEquals(e.getMessage(), "Can't create entity: null");
            return;
        }
        Assertions.fail();
    }

    @Test
    void getRoleByName_userRole_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(USER);
        assertTrue(actual.isPresent());
        assertEquals(USER, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        try {
            Optional<Role> actual = roleDao.getRoleByName(NOT_USER);
        } catch (DataProcessingException e) {
            assertEquals("Couldn't get role by role name: " + NOT_USER, e.getMessage());
        }
    }

    @Test
    void getRoleByName_nullRole_notOk() {
        try {
            Optional<Role> actual = roleDao.getRoleByName(null);
        } catch (DataProcessingException e) {
            assertEquals("Couldn't get role by role name: " + null, e.getMessage());
        }
    }
}
