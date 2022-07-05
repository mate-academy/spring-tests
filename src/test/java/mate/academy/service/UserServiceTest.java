package mate.academy.service;

import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;

class UserServiceTest {
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao,passwordEncoder);
        String correctEmail = "bchupika@mate.academy";
        String password = "12345678";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Long identifier = 1L;
        user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        user.setId(identifier);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual, "User must not be null for: " + user);
    }

    @Test
    void findById_Ok() {
        Long identifier = 1L;;
        Mockito.when(userDao.findById(identifier)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(identifier);
        Assertions.assertTrue(actual.isPresent(), "true is expected for identifier: " + identifier);
    }

    @Test
    void findById_nonExistentUser_NotOk() {
        Long incorrectIdentifier = 2L;
        Mockito.when(userDao.findById(incorrectIdentifier)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(incorrectIdentifier);
        Assertions.assertTrue(actual.isEmpty(), "true is expected for identifier: " + incorrectIdentifier);
    }

    @Test
    void findByEmail_Ok() {
        String correctEmail = "bchupika@mate.academy";
        Mockito.when(userDao.findByEmail(correctEmail)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(correctEmail);
        Assertions.assertTrue(actual.isPresent(), "true expected for email: " + correctEmail);
    }

    @Test
    void findByEmail_nonExistentEmail_NotOk() {
        String incorrectEmail = "safasffsa@asfsfa";
        Mockito.when(userDao.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(incorrectEmail);
        Assertions.assertTrue(actual.isEmpty(), "true expected for email: " + incorrectEmail);
    }
}
