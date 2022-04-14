package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String USER_ROLE = "USER";
    private static final String INVALID_ROLE = "MANAGER";
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(USER_ROLE, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notOk() {
        try {
            roleDao.getRoleByName(INVALID_ROLE);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                                                    + INVALID_ROLE, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
