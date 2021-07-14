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
    private static final String EMAIL = "lucy1234@gmail.com";
    private static final String PASSWORD = "12345tyr";
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
    void saveValidEntity_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
        User userFromDB = userDao.save(user);
        Assertions.assertNotNull(userFromDB);
        Assertions.assertEquals(1L, userFromDB.getId());
    }

    @Test
    void findByEmail_validEmail_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
        User userFromDB = userDao.save(user);
        Optional<User> userOptional = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(1L, userOptional.get().getId());
    }

    @Test
    void findByEmail_invalidEmail_Ok() {
        Optional<User> userOptional = userDao.findByEmail("random-email@i.com");
        Assertions.assertFalse(userOptional.isPresent());
    }

    @Test
    void findById_validId_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.emptySet());
        User userFromDB = userDao.save(user);
        Optional<User> userOptional = userDao.findById(userFromDB.getId());
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(userFromDB.getId(), userOptional.get().getId());
    }

    @Test
    void findById_invalidId_ok() {
        Assertions.assertFalse(userDao.findById(2L).isPresent());
    }
}