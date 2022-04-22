package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "12345";
    private static final Long USER_ID = 1L;
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);
    }

    @Test
    void findByEmail() {
        User savedUser = userDao.save(user);
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void save_OK() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_ID, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
    }
}
