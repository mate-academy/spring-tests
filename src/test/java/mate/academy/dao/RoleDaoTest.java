package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ROLE = Role.RoleName.USER.name();
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.valueOf(ROLE));
        roleDao.save(role);
    }

    @Test
    void save_Ok() {
        assertNotNull(role);
        assertEquals(1L, role.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(ROLE);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(Role.RoleName.valueOf(ROLE), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        String invalidRole = "felnfanfa";
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(invalidRole));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
