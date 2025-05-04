package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private UserDao userDao;
    private RoleDao roleDao;
    private Role role;
    private User user;

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
        return new Class<?>[]{User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void save_UserNull_notOk() {
        DataProcessingException dataProcessingExceptionExpected =
                Assertions.assertThrows(DataProcessingException.class, () -> {
                    userDao.save(null);
                }, "DataProcessingException expected");
        Assertions.assertEquals("Can't create entity: null",
                dataProcessingExceptionExpected.getLocalizedMessage());
    }

    @Test
    void findByEmail_OK() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail("bob@i.ua");
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_NotCorrectEmail_NotOK() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userDao.findByEmail("babagalamaga@dohast.ua").get();
        }, "NoSuchElementException expected");

    }
}
