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

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.ADMIN);
    }

    @Test
    public void save_Ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        assertTrue(actual.isPresent());
        assertEquals(Role.RoleName.ADMIN, actual.get().getRoleName());
    }

    @Test
    public void getRoleByName_NotOk() {
        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("ROLE"));
        assertEquals("Couldn't get role by role name: ROLE", exception.getMessage());
    }
}
