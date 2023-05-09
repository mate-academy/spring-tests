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
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        User actualUser = userService.save(expectedUser);
        Assertions.assertEquals(actualUser.getEmail(), EMAIL);
    }

    @Test
    void getById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findById(1L).get();
        Assertions.assertEquals(actualUser.getId(), 1L);
    }

    @Test
    void getByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findByEmail(EMAIL).get();
        Assertions.assertEquals(actualUser.getEmail(), EMAIL);
    }
}
