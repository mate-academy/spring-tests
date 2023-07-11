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

class UserServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "NonExistingEmail";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        User user = createUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_Ok() {
        User user = createUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_invalidId_Ok() {
        User user = createUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User user = createUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_Ok() {
        User user = createUser(1L, EMAIL, PASSWORD);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createUser(Long id, String email, String password) {
        User bob = new User();
        bob.setId(id);
        bob.setEmail(email);
        bob.setPassword(password);
        return bob;
    }
}
