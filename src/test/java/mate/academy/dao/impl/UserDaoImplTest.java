package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
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
    void save_correctUser_isOk() {
        User user = new User();
        user.setPassword("12345");
        user.setEmail("user@gmail.com");
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_correctEmail_isOk() {
        User user = new User();
        user.setPassword("12345");
        user.setEmail("user@gmail.com");
        user = userDao.save(user);
        Optional<User> actual = userDao.findByEmail("user@gmail.com");
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_notExistentEmail_isEmpty() {
        User user = new User();
        user.setPassword("12345");
        user.setEmail("user@gmail.com");
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("undefined@gmail.com");
        Assertions.assertEquals(actual, Optional.empty());
    }

    @Test
    void findById_correctId_isOk() {
        User user = new User();
        user.setPassword("12345");
        user.setEmail("user@gmail.com");
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_notExistId_notOk() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }
}
