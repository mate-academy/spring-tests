package mate.academy.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String CORRECT_EMAIL = "bob@i.ua";
    private static final String WRONG_EMAIL = "else@i.ua";
    private static final String PASSWORD = "1234";
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
        user.setEmail(CORRECT_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_UserExists_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_UserNotExists_NotOk() {
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_UserExists_Ok() {
        Mockito.when(userDao.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(CORRECT_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_UserDoesNotExist_NotOk() {
        Mockito.when(userDao.findByEmail(WRONG_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(WRONG_EMAIL);
        Assertions.assertTrue(actual.isPresent());
    }
}
