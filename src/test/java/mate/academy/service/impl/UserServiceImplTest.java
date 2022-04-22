package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "12345";
    private static final Long USER_ID = 1L;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(USER_ID);
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);
    }

    @Test
    void save_OK() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(any())).thenReturn(user.getPassword());
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(USER_ID, actual.getId());
    }

    @Test
    void findById_OK() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(USER_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getClass(), actual.get().getClass());
    }

    @Test
    void findByEmail_OK() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getClass(), actual.get().getClass());
    }
}
