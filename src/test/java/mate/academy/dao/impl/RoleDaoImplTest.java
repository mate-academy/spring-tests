package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(RoleName.USER);
        roleDao.save(roleUser);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleUser);
        assertNotNull(actual);
        assertEquals(actual, roleUser);
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(RoleName.USER.name());
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getRoleName().name(), roleUser.getRoleName().name());
    }

    @Test
    void getNonExistentRole_NotOk() {
        Optional<Role> actual = roleDao.getRoleByName(RoleName.ADMIN.name());
        assertFalse(actual.isPresent());
    }
}
