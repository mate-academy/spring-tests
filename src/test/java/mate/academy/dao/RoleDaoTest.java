package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private Role testRole;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        testRole = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(testRole);
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(1L, actual.getId(),
                String.format("Role id after save should be 1, but was: %d", actual.getId()));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(testRole);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(testRole.getRoleName().name(), actual.get().getRoleName().name(),
                String.format("Should return: %s, but was: %s",
                        testRole.getRoleName().name(), actual.get().getRoleName().name()));
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

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
