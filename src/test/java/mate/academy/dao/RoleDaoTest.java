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
    private static final String INVALID_ROLE_NAME = "INVALID_NAME";
    private RoleDao roleDao;
    private Role defaultRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        defaultRole = new Role();
        Role.RoleName userRoleName = Role.RoleName.valueOf("USER");
        defaultRole.setRoleName(userRoleName);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(defaultRole);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_validRole_Ok() {
        Role actual = roleDao.save(defaultRole);
        Optional<Role> roleByName = roleDao.getRoleByName("USER");
        assertTrue(roleByName.isPresent());
        assertEquals(defaultRole.getRoleName(), roleByName.get().getRoleName());
    }

    @Test
    void getRoleByName_invalidRole_DataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(INVALID_ROLE_NAME));
    }
}
