package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private final String userRole = "USER";

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_validName_ok() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        Optional<Role> actual = roleDao.getRoleByName(userRole);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                "It should return optional with '%s' role for role name '%s'\n"
                        .formatted(expected.getRoleName(), userRole));
    }

    @Test
    void getRoleByName_validRoleNameMissingInDb_ok() {
        Optional<Role> actual = roleDao.getRoleByName(userRole);
        Assertions.assertTrue(actual.isEmpty(),
                "It should return empty optional for valid, but unregistered to db role\n");
    }

    @Test
    void getRoleByName_nonExistingRoleName_notOk() {
        String roleName = "DOG";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(roleName),
                "It should throw %s for non existing role name '%s'\n"
                        .formatted(DataProcessingException.class, roleName));
    }
}
