package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
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
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private RoleDao roleDao;
    private User bob;
    private Role role;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        roleDao = Mockito.mock(RoleDao.class);
        role = new Role(Role.RoleName.USER);
        bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(USER_PASSWORD, actual.getPassword()));
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));
        Optional<User> optionalUser = userDao.findById(bob.getId());
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findById_IsNotFoundId_NotOk() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(bob.getId()).get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> optionalUser = userDao.findByEmail(USER_EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_IsIncorrectEmail_NotOk() {
        Mockito.when(userDao.findByEmail(null)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(null).get());
    }
}
