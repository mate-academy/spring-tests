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
    private UserDao userDao;
    private String password;
    private String email;
    private User bobby;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        password = "iPhone13";
        email = "bobby@apple.com";
        bobby = new User();
        bobby.setEmail(email);
        bobby.setPassword(password);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bobby);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bobby);
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertTrue(actual.isPresent(), "User " + email + "is present in DB" );
        Assertions.assertEquals(email, actual.get().getEmail());
        Assertions.assertEquals(password, actual.get().getPassword());
    }
}
