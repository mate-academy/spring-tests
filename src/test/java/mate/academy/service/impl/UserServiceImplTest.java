package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void save_ok() {
        Mockito.when(userService.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual,
                "Should not be null after saving new user");
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent(),
                "Should not return empty Optional for valid email");
        Assertions.assertEquals(actual.get().getEmail(), EMAIL,
                "User's email should be the same as the one used for search");
        Assertions.assertEquals(actual.get().getPassword(), PASSWORD,
                "Should return correct password for valid input");
    }

    @Test
    void findById_ok() {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent(),
                "Should not return empty Optional for valid id");
        Assertions.assertEquals(actual.get().getEmail(), EMAIL,
                "Should return correct email for valid input");
        Assertions.assertEquals(actual.get().getPassword(), PASSWORD,
                "Should return correct password for valid input");
    }
}
