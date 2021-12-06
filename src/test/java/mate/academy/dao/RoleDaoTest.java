package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static RoleDao roleDao;
    private static Role adminRole;
    private static Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = new Role(Role.RoleName.ADMIN);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(adminRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(adminRole,actual);
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(userRole);
        Optional<Role> actual = roleDao.getRoleByName(userRole.getRoleName().toString());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(userRole,actual.get());
    }

    @Test
    void getRoleByName_NoExistValue() {
        roleDao.save(userRole);
        if (roleDao.getRoleByName(adminRole.getRoleName().toString()).isEmpty()) {
            return;
        }
        Assertions.fail("We shouldn't get no existing role from db");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
