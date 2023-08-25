package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ROLE_NAME_USER = "USER";
    private static final String WRONG_ROLE_NAME = "TEST";
    private RoleDao roleDao;
    private Role testRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        testRole = roleDao.save(new Role(Role.RoleName.USER));
    }

    @Test
    void save_Ok() {
        Role roleAdmin = roleDao.save(new Role(Role.RoleName.ADMIN));
        Assertions.assertNotNull(roleAdmin);
        Assertions.assertEquals(Role.RoleName.ADMIN, roleAdmin.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> roleUser = roleDao.getRoleByName(ROLE_NAME_USER);
        if (roleUser.isEmpty()) {
            Assertions.fail("Expect optional role");
        }
        Assertions.assertNotNull(roleUser.get());
        Assertions.assertEquals(testRole.getId(), roleUser.get().getId());
    }

    @Test
    void getRoleByName_NotOk() {
        try {
            Optional<Role> roleUser = roleDao.getRoleByName(WRONG_ROLE_NAME);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + WRONG_ROLE_NAME, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
