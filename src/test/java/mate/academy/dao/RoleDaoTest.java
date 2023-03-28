package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String USER = "USER";
    private static final String NOT_USER = "NOT_USER";
    private static final Role userRole = new Role(Role.RoleName.USER);
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
        Role actual = roleDao.save(userRole);
        assertNotNull(actual);
        assertEquals(USER, actual.getRoleName().name());
    }

    @Test
    void save_nullRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_userRole_Ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName(USER);
        assertTrue(actual.isPresent());
        assertEquals(USER, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(NOT_USER));
    }

    @Test
    void getRoleByName_nullRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }
}
