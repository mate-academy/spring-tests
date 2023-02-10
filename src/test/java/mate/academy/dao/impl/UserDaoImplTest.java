package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mate.academy.model.User;
import mate.academy.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private User expected;
    private static final String CORRECT_EMAIL = "rita.jones@gmail.com";
    private static final String INVALID_EMAIL = "sdf@.s";
    private static final String PASSWORD = "454poiDFG";
    private static final Long CORRECT_ID = 1L;
    private static final Long INCORRECT_ID = 100500L;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());

        expected = new User();
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CORRECT_ID, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
    }

    @Test
    void findById_Ok() {
        userDao.save(expected);

        Optional<User> optionalUser = userDao.findById(CORRECT_ID);
        if (optionalUser.isEmpty()) {
            Assertions.fail("Optional must be present");
        }

        User actual = optionalUser.get();
        Assertions.assertEquals(CORRECT_ID, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
    }

    @Test
    void findById_NotOk() {
        Optional<User> actual = userDao.findById(INCORRECT_ID);
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findByEmail_Ok() {
        User expected = new User();
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
        userDao.save(expected);

        Optional<User> optionalUser = userDao.findByEmail(CORRECT_EMAIL);
        if (optionalUser.isEmpty()) {
            Assertions.fail("Optional must be present");
        }

        User actual = optionalUser.get();
        Assertions.assertEquals(CORRECT_ID, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<Object> expected = Optional.empty();
        Optional<User> actual = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertEquals(expected, actual);
    }
}
