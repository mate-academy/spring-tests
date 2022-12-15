package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static UserDao userDao;
    private static UserService userService;
    private static User user;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        role = new Role();
        user = new User();
    }

    @BeforeEach
    void setUp() {
        role.setRoleName(ROLE);

        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);

        User actual = userService.save(user);
        Assertions.assertNotNull(actual, "Method must return User object");
        Assertions.assertEquals(user, actual,
                "Expected " + user + ", but was " + actual);
    }

    @Test
    void findById_Ok() {
        Long id = 1L;
        user.setId(id);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));

        User actual = userService.findById(id).get();
        Assertions.assertNotNull(actual, "Method must return User object");
        Assertions.assertEquals(user, actual,
                "Expected " + user + ", but was " + actual);
        Assertions.assertEquals(user.getId(), actual.getId(),
                "Expected " + user.getId() + ", but was " + actual.getId());
    }

    @Test
    void findById_NotOk() {
        Long id = 1L;
        Mockito.when(userDao.findById(id)).thenReturn(null);

        Optional<User> actual = userService.findById(id);
        Assertions.assertNull(actual, "Expected null value, but was " + actual);
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User actual = userService.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual, "Method must return User object");
        Assertions.assertEquals(user, actual,
                "Expected " + user + ", but was " + actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail(),
                "Expected " + user.getEmail() + ", but was " + actual.getEmail());
    }

    @Test
    void findByEmail_NotOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(null);

        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertNull(actual, "Expected null value, but was " + actual);
    }
}
