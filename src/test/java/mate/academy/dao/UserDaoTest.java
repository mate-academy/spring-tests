package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "123456";
    private Role role;
    private User user;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }
}
