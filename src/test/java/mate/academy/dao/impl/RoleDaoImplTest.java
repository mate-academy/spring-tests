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
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertNotNull(actual.get());
        assertEquals(userRole, actual.get());
    }

    @Test
    void getRoleByName_NullRole_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }

    @Test
    void getRoleByName_NoSuchRole_NotOk() {
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        assertTrue(actual.isEmpty());
    }
}
