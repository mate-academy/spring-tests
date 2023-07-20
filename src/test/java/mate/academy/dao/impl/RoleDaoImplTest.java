package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void saveRole_Ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(role, actual);
        assertEquals(1L, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void saveRole_Not_Ok() {
        assertThrows(DataProcessingException.class, () ->
                roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(role.getRoleName().name());
        assertTrue(actual.isPresent());
        assertEquals(role.getRoleName(), actual.get().getRoleName());
        assertEquals(role.getId(), actual.get().getId());
    }

    @Test
    void getRoleByName_Not_Ok() {
        assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("Wrong name"));
    }
}
