package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String VALID_ROLE_NAME = "USER";
    private static final String INVALID_ROLE_NAME = "BOSS";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(VALID_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_ROLE_NAME, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByInvalidName_notOk() {
        roleDao.save(role);
        try {
            roleDao.getRoleByName(INVALID_ROLE_NAME);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                    + INVALID_ROLE_NAME, e.getMessage());
        }
    }
}
