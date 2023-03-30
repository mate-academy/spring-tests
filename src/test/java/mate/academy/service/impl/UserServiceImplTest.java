package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(VALID_PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findByInvalidId_notOk() {
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(2L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findByInvalidEmail_notOk() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
    }
}
