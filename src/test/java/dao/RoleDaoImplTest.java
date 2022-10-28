package dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
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
        role = new Role(Role.RoleName.USER);
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
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertTrue(actual.isPresent());
        assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    public void getRoleByName_NotOk() {
        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("MODERATOR"));
        assertEquals("Couldn't get role by role name: MODERATOR", exception.getMessage());
    }
}
