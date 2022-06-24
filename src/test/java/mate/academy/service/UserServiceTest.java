package mate.academy.service;

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
import java.util.Optional;
import java.util.Set;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private String correctEmail;
    private String incorrectEmail;
    private String password;
    private Set<Role> roles;
    private Long identifier;
    private Long incorrectIdentifier;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao,passwordEncoder);
        correctEmail = "bchupika@mate.academy";
        incorrectEmail = "safasffsa@asfsfa";
        password = "12345678";
        roles = Set.of(new Role(Role.RoleName.USER));
        identifier = 1L;
        incorrectIdentifier = 2L;
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setId(identifier);
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual, "User must not be null for: " + user);
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setId(identifier);
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        Mockito.when(userDao.findById(identifier)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(identifier);
        Assertions.assertTrue(actual.isPresent(), "true is expected for identifier: " + identifier);
    }

    @Test
    void findById_NotOk() {
        User user = new User();
        user.setId(identifier);
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        Mockito.when(userDao.findById(incorrectIdentifier)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(incorrectIdentifier);
        Assertions.assertTrue(actual.isEmpty(), "true is expected for identifier: " + incorrectIdentifier);
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setId(identifier);
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        Mockito.when(userDao.findByEmail(correctEmail)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(correctEmail);
        Assertions.assertTrue(actual.isPresent(), "true expected for email: " + correctEmail);
    }

    @Test
    void findByEmail_NotOk() {
        User user = new User();
        user.setId(identifier);
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        Mockito.when(userDao.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(incorrectEmail);
        Assertions.assertTrue(actual.isEmpty(), "true expected for email: " + incorrectEmail);
    }
}