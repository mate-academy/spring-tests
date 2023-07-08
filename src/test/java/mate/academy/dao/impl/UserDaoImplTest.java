package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        user = new User();
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(role));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL_OK);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findByEmail_IsIncorrectEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("Wrong email").get());
    }

    @Test
    void saveUser_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void saveUser_Not_Ok() {
        Assertions.assertThrows(DataProcessingException.class,() ->
                userDao.save(null));
    }

    @Test
    void findUserById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findUserById_Not_Ok() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao.findById(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
