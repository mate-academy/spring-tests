package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String EMAIL = "bob@mail.com";
    private static final String PASSWORD = "1234";
    private static final String WRONG_EMAIL = "wrongEmail@mail.com";
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private static UserDao userDao;
    private User testUser;

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
    }

    @Test
    void save_Ok() {
        User user = createTestUser();
        Mockito.when(userDao.save(user)).thenReturn(testUser);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findById(testUser.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(testUser, actual.get());
    }

    @Test
    void findById_NotOk() {
        Mockito.when(userDao.findById(testUser.getId())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(testUser.getId());
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(testUser, actual.get());
    }

    @Test
    void findByEmail_NotOk() {
        Mockito.when(userDao.findByEmail(WRONG_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(WRONG_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role roleUser = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(roleUser));
        return user;
    }
}
