package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final String ROLE_NAME = "USER";
    private static final String ROLE_NAME_IS_NOT_IN_DB = "PHANTOM";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> optionalRole = roleDao.getRoleByName(ROLE_NAME);
        Assertions.assertTrue(optionalRole.isPresent());
        Role.RoleName actual = optionalRole.get().getRoleName();
        Assertions.assertEquals(role.getRoleName(), actual);
    }

    @Test
    void getRoleByName_roleIsNotInDb_ok() {
        roleDao.save(role);
        try {
            roleDao.getRoleByName(ROLE_NAME_IS_NOT_IN_DB);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get role by role name: "
                            + ROLE_NAME_IS_NOT_IN_DB, e.getMessage());
        }
    }
}
