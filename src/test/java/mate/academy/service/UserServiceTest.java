package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String USER_EMAIL = "bob@bob.ua";
    private static final String USER_PASSWORD = "12345678";
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(USER_PASSWORD, actual.getPassword()));
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_IdNonExists_NotOk() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertTrue(userOptional.isEmpty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userOptional.get(), "NoSuchElementException expected");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(USER_EMAIL);
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(userOptional);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_InvalidEmail_NotOk() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(USER_EMAIL);
        Assertions.assertTrue(userOptional.isEmpty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userOptional.get(), "NoSuchElementException expected");
    }
}
