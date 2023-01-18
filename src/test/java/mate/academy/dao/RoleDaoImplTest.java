package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class RoleDaoImplTest extends AbstractTest {
    private static final String ROLE_NAME = Role.RoleName.USER.name();
    private static final String INVALID_ROLE_NAME = Role.RoleName.USER.name() + "!";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(ROLE_NAME);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NoRoleInDB_NotOk() {
        Optional<Role> actual = roleDao.getRoleByName(ROLE_NAME);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getRoleByName_DataProcessingException_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(INVALID_ROLE_NAME));
    }
}
