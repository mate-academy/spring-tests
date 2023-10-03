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
    private static final String TEST_EMAIL = "test@i.ua";
    private static final String TEST_PASSWORD = "password";
    private static final Long USER_ID = 1L;
    private User user;
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(USER_ID);
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        Mockito.when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(user);
    }

    @Test
    void save_ok() {
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_ID, actual.getId(),
                String.format("Expected id should be %s, but was %s",
                        USER_ID, actual.getId()));
        Assertions.assertEquals(TEST_EMAIL, actual.getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.getEmail()));
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(USER_ID)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(USER_ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.get().getId()));
    }

    @Test
    void findById_nonExistId_notOk() {
        Mockito.when(userDao.findById(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userService.findById(99L).get(),
                "Expected NoSuchElementException to be thrown for non-existed id");
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(TEST_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(USER_ID, actual.get().getId(),
                String.format("Expected id of user should be %s, but was %s",
                        USER_ID, actual.get().getId()));
    }

    @Test
    void findByEmail_nonExistedEmail_notOk() {
        Mockito.when(userDao.findByEmail("unknown")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userService.findByEmail("unknown").get(),
                "Expected NoSuchElementException to be thrown for non-existed email");
    }
}
