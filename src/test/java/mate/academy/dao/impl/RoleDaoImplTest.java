package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final long ADMIN_ROLE_ID = 1L;
    private static final long USER_ROLE_ID = 2L;
    private static final String NON_EXIST_ROLE = "CUSTOMER";
    private static Role adminRole;
    private static Role userRole;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        adminRole = new Role(Role.RoleName.ADMIN);
        userRole = new Role(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validRoles_Ok() {
        Role actualAdmin = roleDao.save(adminRole);
        Role actualUser = roleDao.save(userRole);
        Assertions.assertNotNull(actualAdmin);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(ADMIN_ROLE_ID, actualAdmin.getId());
        Assertions.assertEquals(USER_ROLE_ID, actualUser.getId());
        Assertions.assertEquals(adminRole.getRoleName(), actualAdmin.getRoleName());
        Assertions.assertEquals(userRole.getRoleName(), actualUser.getRoleName());
    }

    @Test
    void getRoleByName_existRole_Ok() {
        roleDao.save(adminRole);
        String existRole = adminRole.getRoleName().name();
        Optional<Role> actual = roleDao.getRoleByName(existRole);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(adminRole.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_nonExistRole_Ok() {
        roleDao.save(adminRole);
        roleDao.save(userRole);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(NON_EXIST_ROLE),
                "Expected to receive DataProcessingException");
    }
}
