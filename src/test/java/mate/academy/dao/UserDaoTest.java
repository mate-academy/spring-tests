package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@bob.ua";
    private static final String INVALID_EMAIL = "alice@bob.ua";
    private static final String USER_PASSWORD = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

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

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void save_UserNull_NotOk() {
        DataProcessingException dataProcessingExceptionExpected =
                Assertions.assertThrows(DataProcessingException.class,
                        () -> userDao.save(null), "DataProcessingException expected");
        Assertions.assertEquals("Can't create entity: null",
                dataProcessingExceptionExpected.getMessage());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(USER_EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_IncorrectEmail_NotOk() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(optionalUser.isPresent());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> optionalUser.get());
    }

    @Test
    void findById_Ok() {
        User savedUser = userDao.save(user);
        Long actual = savedUser.getId();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, 1L);
    }
}
