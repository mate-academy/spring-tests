package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void getRollName_ok() {
        Role savedRole = roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getRoleName(), Role.RoleName.USER);
    }

    @Test
    void getRollName_invalidRollName_notOk() {
        assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(null));
    }
}
