package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static Role USER = new Role(Role.RoleName.USER);
    private static Role ADMIN = new Role(Role.RoleName.ADMIN);
    private static String USER_ROLE_NAME = "USER";
    private static String ADMIN_ROLE_NAME = "ADMIN";
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validRole_ok() {
        Role actual = roleDao.save(USER);
        Long expectedId = 1L;
        String expectedRoleName = USER_ROLE_NAME;
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedRoleName, actual.getRoleName().name());
    }

    @Test
    void save_twoRoles_ok() {
        Role userActual = roleDao.save(USER);
        Role adminActual = roleDao.save(ADMIN);
        Long userExpectedId = 1L;
        Long adminExpectedId = 2L;
        assertEquals(adminExpectedId, adminActual.getId());
        String userRoleNameExpected = USER_ROLE_NAME;
        String adminRoleNameExpected = ADMIN_ROLE_NAME;
        assertEquals(userExpectedId, userActual.getId());
        assertEquals(adminRoleNameExpected, adminActual.getRoleName().name());
        assertEquals(userRoleNameExpected, userActual.getRoleName().name());
    }

    @Test
    void save_theSameRoleTwice_ok() {
        roleDao.save(USER);
        Role actual = roleDao.save(USER);
        Long expected = 2L;
        assertEquals(expected, actual.getId());
    }

    @Test
    void getByRoleName_validInput_ok() {
        Role expected = roleDao.save(USER);
        Optional<Role> actual = roleDao.getRoleByName(USER.getRoleName().name());
        if (actual.isEmpty()) {
            fail();
        }
        assertEquals(expected.getId(), actual.get().getId());
        assertEquals(expected.getRoleName().name(), actual.get().getRoleName().name());
    }

    @Test
    void getByRoleName_twoRoles_ok() {
        Role userExpected = roleDao.save(USER);
        roleDao.save(ADMIN);
        Optional<Role> actual = roleDao.getRoleByName(USER.getRoleName().name());
        if (actual.isEmpty()) {
            fail();
        }
        assertEquals(userExpected.getId(), actual.get().getId());
        assertEquals(userExpected.getRoleName().name(), actual.get().getRoleName().name());
    }

    @Test
    void getByRoleName_emptyDB_ok() {
        Optional<Role> actual = roleDao.getRoleByName(USER.getRoleName().name());
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}
