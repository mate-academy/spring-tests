package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleService roleService;
    private String email;
    private String password;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        email = "bob@i.ua";
        password = "1234";
        bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent(), "Email was not found");
        Assertions.assertEquals(email, actual.get().getEmail(), "Email is not equal");
        Assertions.assertEquals(password, actual.get().getPassword(), "Password isn't equal");
    }
}
