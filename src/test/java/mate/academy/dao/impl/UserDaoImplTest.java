package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private User user;
    private Role role;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(new Role(Role.RoleName.USER));
        user = new User(EMAIL, PASSWORD, Set.of(role));
    }

    @Test
    void saveUser_ok() {
        User actualUser = userDao.save(user);
        Assertions.assertNotNull(actualUser.getId());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getRoles(), actualUser.getRoles());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(EMAIL, optionalUser.get().getEmail());
    }

    @Test
    void findByEmail_NotExistEmail_Empty() {
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isEmpty());
    }
}
