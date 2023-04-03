package mate.academy.service.impl;

import java.util.NoSuchElementException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;
    private static UserDao userDao;
    private static final String TEST_EMAIL = "email@test.test";
    private static final String FAKE = "fake@fake.fake";
    private static final String PASSWORD = "password";
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userDao.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void save_ok() {
        User actual = userService.save(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_userExists_ok() {
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getEmail(), user.getEmail());
        Assertions.assertEquals(actual.get().getPassword(), user.getPassword());
    }

    @Test
    void findById_userWithSuchIdDoesNotExists_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(4L).get());
    }

    @Test
    void findByEmail_userExists_ok() {
        Optional<User> actual = userService.findByEmail(TEST_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_userWithSuchEmailDoesNotExists_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(FAKE).get());
    }
}
