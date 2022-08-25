package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Role expectedRole = new Role();
        expectedRole.setRoleName(Role.RoleName.USER);
        Role actualRole = roleDao.save(expectedRole);
        assertNotNull(actualRole);
        assertEquals(1L, actualRole.getId());
        assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void save_nullRole_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null),
                "You should throw DataProcessingException if role is null");
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_nullRoleName_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null),
                "You should throw DataProcessingException if roleName is null");
    }
}
