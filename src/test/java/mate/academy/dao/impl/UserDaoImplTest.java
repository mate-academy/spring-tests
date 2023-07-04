package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private RoleDao roleDao;
    private Role role;
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        User actual = userDao.findById(1L).get();
        Assertions.assertNotNull(actual);
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actualOptional = userDao.findByEmail(USER_EMAIL);
        Assertions.assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_Not_Ok() {
        userDao.save(user);
        Optional<User> actualOptional = userDao.findByEmail("alice.ua");
        Assertions.assertFalse(actualOptional.isPresent());
    }
}
