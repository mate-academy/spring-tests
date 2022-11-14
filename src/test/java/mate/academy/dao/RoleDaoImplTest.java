package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String USER_ROLE_NAME = "USER";
    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String WRONG_ROLE_NAME = "WRONG";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final Role ADMIN_ROLE = new Role(Role.RoleName.ADMIN);
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(USER_ROLE);
        roleDao.save(ADMIN_ROLE);
    }

    @Test
    void getRoleByName_User() {
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE_NAME);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getRoleName(), USER_ROLE.getRoleName());
    }

    @Test
    void getRoleByName_Admin() {
        Optional<Role> actual = roleDao.getRoleByName(ADMIN_ROLE_NAME);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getRoleName(), ADMIN_ROLE.getRoleName());
    }

    @Test
    void getRoleByName_NotExistRoleName() {
        try {
            roleDao.getRoleByName(WRONG_ROLE_NAME);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + WRONG_ROLE_NAME, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException!");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
