package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    UserDao userDao;

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
    void findByEmail_Ok() {
        String email = "bob@i.ua";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        userDao.save(bob);

        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.get().getEmail(), "Email is not equal");
        Assertions.assertEquals(password, actual.get().getPassword(), "Password is not equal");
    }
}