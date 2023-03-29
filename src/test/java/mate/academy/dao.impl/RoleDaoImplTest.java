package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static RoleDaoImpl roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{ Role.class };
    }

    @Test
    void save_saveRole_ok() {
        Role roleUser = new Role();
        roleDao.save(roleUser);

        assertNotNull(roleUser);
        assertEquals(1L, roleUser.getId());
    }

    @Test
    void getRoleByName_twoRolesSavedGetRight_ok() {
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        roleDao.save(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(roleAdmin);

        Optional<Role> actualRole = roleDao.getRoleByName(roleUser.getRoleName().name());
        assertTrue(actualRole.isPresent(),
                "Expected to find some role when getting by saved role name");
        assertEquals(roleUser.getRoleName(), actualRole.get().getRoleName(),
                "Expected to names match");
    }

    @Test
    void getRoleByName_roleIsNotExist_notOk() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for role name: USER, "
                        + "but was: %s", actual));
    }

    @Test
    void getRoleByName_roleNameIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            roleDao.getRoleByName(null);
        });
    }

    @Test
    void getRoleByName_nonExistingRoleName_notOk() {
        String roleName = "NON_EXISTING_ROLE";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(roleName),
                "Method should throw %s for non existing roleName '%s'\n"
                        .formatted(DataProcessingException.class, roleName));
    }
}
