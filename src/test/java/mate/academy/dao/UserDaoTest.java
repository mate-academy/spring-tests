package mate.academy.dao;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "my@i.ua";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
    }

    @Test
    void save_notExistingUser_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_userWithExistingId_notOk() {
        userDao.save(user);
        user.setId(1L);
        try {
            userDao.save(user);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("my@i.ua");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_existingId_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals("my@i.ua", actual.get().getEmail());
        Assertions.assertEquals("12345678", actual.get().getPassword());
    }

    @Test
    void findById_notExistingId_ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}