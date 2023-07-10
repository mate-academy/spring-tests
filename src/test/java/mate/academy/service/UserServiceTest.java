package mate.academy.service;

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
    private static final String EMAIL = "bob@i.ua";
    private static final String NOT_EXISTED_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_user_ok() {
        User user = getUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_userWithId_ok() {
        User user = getUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_notExistedUserWithId_notOk() {
        Optional<User> actual = userService.findById(10L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_validEmail_ok() {
        User user = getUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_notExistedEmail_notOk() {
        Optional<User> actual = userService.findByEmail(NOT_EXISTED_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User getUser(Long id, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}

