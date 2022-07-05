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
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        String email = "bob@i.ua";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);

        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertTrue(actual.isPresent(),
                "For existing user with valid email Optional should no be empty");
        Assertions.assertEquals(1L, actual.get().getId());
    }
}
