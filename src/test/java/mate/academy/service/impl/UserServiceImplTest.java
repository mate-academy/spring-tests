package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
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
    private static final String VALID_EMAIL = "bob";
    private static final String VALID_PASSWORD = "1234";
    private static final String ENCODED_PASSWORD = "encoded1234";
    private static final String NOT_VALID_EMAIL = "taras";
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        bob = new User();
        bob.setEmail(VALID_EMAIL);
        bob.setPassword(VALID_PASSWORD);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        Mockito.when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        User actual = userService.save(bob);
        Assertions.assertEquals(ENCODED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(VALID_EMAIL, actual.getEmail());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(NOT_VALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertNotEquals(Optional.of(bob), actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            userService.findById(1L).get();
        });
    }
}
