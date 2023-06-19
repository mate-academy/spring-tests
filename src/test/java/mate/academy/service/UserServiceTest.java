package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String USER_MAIL = "example@gmail.com";
    private static final String INVALID_USER_MAIL = "invalid_mail";
    private static final String USER_PASSWORD = "example_password";
    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_MAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void saveUser_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actualUser = userService.save(user);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void findUserById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findUserById_notExist_notOk() {
        Mockito.when(userDao.findById(INVALID_USER_ID)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(INVALID_USER_ID);
        Assertions.assertTrue(userOptional.isEmpty());
        Assertions.assertThrows(NoSuchElementException.class,
                userOptional::get, "NoSuchElementException expected");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(USER_MAIL)).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(USER_MAIL);
        Assertions.assertTrue(userOptional.isPresent());
        User actualUser = userOptional.get();
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Mockito.when(userDao.findByEmail(INVALID_USER_MAIL)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(INVALID_USER_MAIL);
        Assertions.assertTrue(userOptional.isEmpty());
        Assertions.assertThrows(NoSuchElementException.class,
                userOptional::get, "NoSuchElementException expected");
    }
}
