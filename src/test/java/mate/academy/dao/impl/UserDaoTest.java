package mate.academy.dao.impl;

import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    public void findByEmail_ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleDao.save(userRole);
        user.setRoles(Set.of(userRole));
        User expected = userDao.save(user);
        User actual = userDao.findByEmail(expected.getEmail()).get();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(1, actual.getRoles().size());
        Assertions.assertEquals(Role.RoleName.USER, user.getRoles().stream()
                .findFirst().get().getRoleName());
    }

    @Test
    public void findByEmail_notExistingEmail_ok() {
        Assertions.assertTrue(userDao.findByEmail("elon.musk@space.x").isEmpty());
    }

    @Test
    public void findByEmail_nullEmail_ok() {
        Assertions.assertTrue(userDao.findByEmail(null).isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
