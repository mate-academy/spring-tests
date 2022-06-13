package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoleDaoTest extends AbstractTest{
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final Role ADMIN_ROLE = new Role(Role.RoleName.ADMIN);
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role savedUser = roleDao.save(USER_ROLE);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(1L, savedUser.getId());
        assertEquals(USER_ROLE.getRoleName(), savedUser.getRoleName());

        Role savedAdmin = roleDao.save(ADMIN_ROLE);
        assertNotNull(savedAdmin);
        assertNotNull(savedAdmin.getId());
        assertEquals(2L, savedAdmin.getId());
        assertEquals(ADMIN_ROLE.getRoleName(), savedAdmin.getRoleName());
    }

    @Test
    void save_nullRole_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }


    @Test
    void getRoleByName_Ok() {
        roleDao.save(USER_ROLE);
        roleDao.save(ADMIN_ROLE);
        Optional<Role> actualRole = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertNotNull(actualRole);
        assertTrue(actualRole.isPresent());
        assertEquals(Role.RoleName.USER, actualRole.get().getRoleName());

        actualRole = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        assertNotNull(actualRole);
        assertTrue(actualRole.isPresent());
        assertEquals(Role.RoleName.ADMIN, actualRole.get().getRoleName());
    }

    @Test
    void getRoleByName_nonExist_notOk() {
        Optional<Role> actualRole = roleDao.getRoleByName(Role.RoleName.USER.name());
        assertNotNull(actualRole);
        assertTrue(actualRole.isEmpty());
    }

    @Test
    void getRoleByName_nullArg_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null));
    }

    @Override
    protected Class<?>[] entities() {
        Class[] classes = {Role.class};
        return classes;
    }
}