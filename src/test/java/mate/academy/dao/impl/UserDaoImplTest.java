package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserDaoImplTest extends AbstractTest {
    private static final String PASSWORD = "123";
    private static final String LOGIN = "rex@mail.com";
    private static UserService userService;
    private static UserDao userDao;
    private static User user;

    @BeforeAll
    static void setUp() {
        userDao = Mockito.mock(UserDaoImpl.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(LOGIN);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Optional<User> expected = Optional.of(user);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(LOGIN)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(LOGIN);
        Optional<User> expected = Optional.of(user);
        Assertions.assertEquals(actual, expected);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[0];
    }
}
