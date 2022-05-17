package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    public static final String USER_ROLE = "USER";
    public static final String WRONG_ROLE = "SUPER_USER";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void saveMethod_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual, "Saved object can't be null");
        Assertions.assertEquals(actual.getId(), 1L, "Generated id is wrong");
    }

    @Test
    void getRoleByNameMethod_Ok() {
        roleDao.save(role);
        Optional<Role> actualRoleOptional = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertTrue(actualRoleOptional.isPresent(), "Role object can't be null");
        Assertions.assertEquals(USER_ROLE, actualRoleOptional.get().getRoleName().name(),
                "Generated role object is wrong");
    }

    @Test
    void getRoleByName_DataProcessing() {
        try {
            roleDao.getRoleByName(WRONG_ROLE);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: "+ WRONG_ROLE, e.getMessage());
            return;
        }
        Assertions.fail("Expected to throw DataProcessingException");
    }
}
