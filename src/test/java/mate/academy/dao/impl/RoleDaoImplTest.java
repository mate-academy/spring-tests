package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDaoImplTest extends AbstractTest {
    private static final Long ID = 1L;
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static final Role.RoleName ADMIN_ROLE = Role.RoleName.ADMIN;
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
        assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual =
                roleDao.getRoleByName(USER_ROLE.name());
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getRoleName(), USER_ROLE);
    }

    @Test
    void getRoleByName_InvalidName_NotOk() {
        roleDao.save(role);
        Optional<Role> actual =
                roleDao.getRoleByName(ADMIN_ROLE.name());
        assertFalse(actual.isPresent());
    }
}
