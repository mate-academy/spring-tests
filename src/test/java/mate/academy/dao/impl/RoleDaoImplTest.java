package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    public static final String VALID_ROLE = "USER";
    public static final String INVALID_ROLE = "INVALID";
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_invalidRole_ok() {
        Role actual = roleDao.save(userRole);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_validData_ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName(VALID_ROLE);
        assertEquals(1L, actual.get().getId());
        assertEquals(VALID_ROLE, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_invalidRole_notOk() {
        try {
            roleDao.getRoleByName(INVALID_ROLE);
        } catch (DataProcessingException e) {
            assertEquals("Couldn't get role by role name: " + INVALID_ROLE,
                    e.getMessage());
        }
    }
}
