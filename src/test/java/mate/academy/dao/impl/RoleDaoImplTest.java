package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(roleUser);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_roleExists_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        roleDao.save(roleUser);
        Optional<Role> actualRole = roleDao.getRoleByName("USER");
        assertTrue(actualRole.isPresent());
        assertEquals(1L, actualRole.get().getId());
    }

    @Test
    void getRoleByName_roleNotExist_Ok() {
        Optional<Role> actualRole = roleDao.getRoleByName("ADMIN");
        assertTrue(actualRole.isEmpty());
    }
}
