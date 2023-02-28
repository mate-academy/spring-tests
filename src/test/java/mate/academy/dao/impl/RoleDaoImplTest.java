package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Long DEFAULT_ID = 1L;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {
            Role.class
        };
    }

    @Test
    void getRoleByName_roleName_ok() {
        Optional<Role> expectedRole = Optional.of(role);
        Optional<Role> actualRole = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertEquals(expectedRole.get().getRoleName(), actualRole.get().getRoleName());
        Assertions.assertEquals(DEFAULT_ID, actualRole.get().getId());
    }

    @Test
    void getRoleByName_nullRoleName_notOk() {
        Exception exception = assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
        String actualMessage = exception.getMessage();
        String expectedMessage = "Couldn't get role by role name: null";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getRoleByName_invalidRoleName_notOk() {
        Exception exception = assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("MODERATOR"));
        String actualMessage = exception.getMessage();
        String expectedMessage = "Couldn't get role by role name: MODERATOR";
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
