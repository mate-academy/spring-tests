package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final String PASSWORD = "1234";
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        User actualUser = userService.save(expectedUser);
        Assertions.assertEquals(actualUser.getEmail(), EMAIL);
    }

    @Test
    void getById_ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findById(1L).get();
        Assertions.assertEquals(actualUser.getId(), 1L);
    }

    @Test
    void getById_wrongId_notOk() {
        Mockito.when(userDao.findById(-1L)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(-1L));
    }

    @Test
    void getByEmail_ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.findByEmail(EMAIL).get();
        Assertions.assertEquals(actualUser.getEmail(), EMAIL);
    }

    @Test
    void getByEmail_wrongEmail_notOk() {
        Mockito.when(userDao.findByEmail(WRONG_EMAIL)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(WRONG_EMAIL));
    }
}
