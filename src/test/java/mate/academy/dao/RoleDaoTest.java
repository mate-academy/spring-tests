package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role user = new Role();
        user.setRoleName(Role.RoleName.USER);

        Role actual = roleDao.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role user = new Role();
        user.setRoleName(Role.RoleName.USER);
        roleDao.save(user);
        Role actual = roleDao.getRoleByName("USER").get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void getRoleByName_Not_Existing_RoleName() {
        Role user = new Role();
        user.setRoleName(Role.RoleName.USER);
        roleDao.save(user);
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        Assertions.assertTrue(actual.isEmpty());
    }
}
