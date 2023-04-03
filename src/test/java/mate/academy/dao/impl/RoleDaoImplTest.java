package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractDaoTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleName_ok() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getRoleName(), actual.get().getRoleName(),
                String.format("Should return: %s, but returned: %s",
                        expected.getRoleName().name(), actual.get().getRoleName().name()));
    }

    @Test
    void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertEquals(1L, actual.getId(),
                String.format("Id should be 1 after saving, but was %s", actual.getId()));
    }

    @Test
    void roleIsNotExists_notOk() {
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for USER but was: %s",
                        actual));
    }

    @Test
    void roleNameIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            roleDao.getRoleByName(null);
        });
    }
}
