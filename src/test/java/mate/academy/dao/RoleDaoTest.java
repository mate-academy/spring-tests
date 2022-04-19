package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    public static final String CORRECT_ROLE = "USER";
    public static final String INCORRECT_ROLE = "ADMINISTRATOR";
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_notExistingRole_ok() {
        Role actual = roleDao.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_existingRole_ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName(CORRECT_ROLE);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(CORRECT_ROLE, actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        try {
            roleDao.getRoleByName(INCORRECT_ROLE);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: " + INCORRECT_ROLE,
                    e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
