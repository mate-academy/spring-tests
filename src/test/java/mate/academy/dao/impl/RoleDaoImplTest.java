package mate.academy.dao.impl;

import static mate.academy.model.Role.RoleName.USER;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_validRoleName_ok() {
        Role expected = new Role(USER);
        roleDao.save(expected);
        String roleUser = "USER";
        Optional<Role> actual = roleDao.getRoleByName(roleUser);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(
                expected.getRoleName(),
                actual.get().getRoleName(),
                "Method should return optional containing role "
                        + expected.getRoleName()
                        + " for roleName "
                        + roleUser);
    }

    @Test
    void getRoleByName_validRoleNameMissingInDb_ok() {
        String roleUser = "UNKNOWN_ROLE";
        Optional<Role> actual = roleDao.getRoleByName(roleUser);
        Assertions.assertTrue(actual.isEmpty(),
                "Method should return empty optional if roleName is valid, but is missing in db");
    }

    @Test
    void getRoleByName_nonExistingRoleName_notOk() {
        String roleName = "NON_EXISTING_ROLE";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(roleName),
                "Method should throw "
                        + DataProcessingException.class
                        + " for non existing roleName "
                        + roleName);
    }
}
