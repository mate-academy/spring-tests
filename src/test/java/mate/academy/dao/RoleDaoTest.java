package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Role TEST_ROLE = new Role(Role.RoleName.USER);
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(TEST_ROLE);
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(1L, actual.getId(),
                String.format("Role id after save should be 1, but was: %d", actual.getId()));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(TEST_ROLE);
        Optional<Role> actual = roleDao.getRoleByName("USER");
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(TEST_ROLE.getRoleName().name(), actual.get().getRoleName().name(),
                String.format("Should return: %s, but was: %s",
                        TEST_ROLE.getRoleName().name(), actual.get().getRoleName().name()));
    }

    @Test
    void getRoleByName_roleIsNotExist_notOk() {
        Optional<Role> actual = roleDao.getRoleByName("USER");
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
