package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private User user;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.test");
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(any())).thenReturn("1234");
        Mockito.when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.getEmail(), user.getEmail(),
                String.format("Should return user with email: %s, "
                        + "but was: %s", user.getEmail(), actual.getEmail()));
        Assertions.assertEquals(actual.getPassword(), user.getPassword(),
                String.format("Should return user with password: %s, "
                        + "but was: %s", user.getPassword(), actual.getPassword()));
    }

    @Test
    void findById_Ok() {
        user.setId(1L);
        Mockito.when(userDao.findById(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.get().getId(), user.getId(),
                String.format("Should return user with id: %s, "
                        + "but was: %s", user.getId(), actual.get().getId()));
    }

    @Test
    void findById_idIsNotExist_Ok() {
        user.setId(1L);
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(1L);
        Assertions.assertFalse(actual.isPresent(),
                "User should be null for incorrect id");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.get().getEmail(), user.getEmail(),
                String.format("Should return user with email: %s, "
                        + "but was: %s", user.getEmail(), actual.get().getEmail()));
    }

    @Test
    void findByEmail_emailIsNotExist_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertFalse(actual.isPresent(),
                "User should be null for incorrect email");
    }

    @Test
    void findByEmail_emailIsNull_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(null);
        Assertions.assertFalse(actual.isPresent(),
                "User should be null for null email");
    }
}
