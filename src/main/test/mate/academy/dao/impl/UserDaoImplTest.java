package mate.academy.dao.impl;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123456789";
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        User actual = userDao.save(user);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
        User expected = userDao.save(user);

        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
        User expected = userDao.save(user);

        Optional<User> actual = userDao.findById(expected.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }
}
