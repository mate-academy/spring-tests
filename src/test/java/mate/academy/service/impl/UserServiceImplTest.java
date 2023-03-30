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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private UserDao userDao;
    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void findById_Ok() {
        user.setId(1L);
        Mockito.when(userDao.findById(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.get().getId(), user.getId(),
                "Method should return user with id: "
                + user.getId() + " but was: " + actual.get().getId());
    }

    @Test
    void findById_NotOk() {
        user.setId(1L);
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(1L);
        Assertions.assertFalse(actual.isPresent(),
                "User with incorrect id is not exist!");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_NullEmail_NotOk() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(null);
        Assertions.assertFalse(actual.isPresent(),
                "Method should return null");
    }
}
