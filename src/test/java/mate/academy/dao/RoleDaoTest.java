package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setup() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, role.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> resultByName = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(resultByName.isPresent());
        Role actual = resultByName.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_IsNull() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> resultByName = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertFalse(resultByName.isPresent(), "role does`t exist");
    }
}
