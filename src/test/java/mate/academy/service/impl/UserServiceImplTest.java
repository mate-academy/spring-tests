package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "right@email.com";
    private static final String PASSWORD = "12345678";
    private static final Long USER_ID = 1L;
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        expected = new User();
        expected.setEmail(EMAIL);
        expected.setPassword(PASSWORD);
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(any())).thenReturn(expected);

        User actual = userService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(expected));

        User actual = userService.findByEmail(EMAIL).get();

        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_emailNull_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(null).get());
    }

    @Test
    void findByEmail_emailNotExistInDb_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(EMAIL).get());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(USER_ID)).thenReturn(Optional.of(expected));

        User actual = userService.findById(USER_ID).get();

        assertEquals(expected, actual);
    }

    @Test
    void findById_ok_idNull_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findById(null).get());
    }

    @Test
    void findById_idNotExistInDb_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findById(USER_ID).get());
    }
}
