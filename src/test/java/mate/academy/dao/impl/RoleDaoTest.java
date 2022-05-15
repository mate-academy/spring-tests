package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE_USER = Role.RoleName.USER;
    private static RoleDao roleDao;
    private static Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(ROLE_USER);
    }

    @BeforeEach
    void setUp() {
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
        Role actual = roleDao.getRoleByName(ROLE_USER.name()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE_USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_DataProcessingException() {
        String roleName = "INCORRECT_ROLE";
        roleDao.save(role);
        try {
            roleDao.getRoleByName(roleName);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: INCORRECT_ROLE",
                    e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
