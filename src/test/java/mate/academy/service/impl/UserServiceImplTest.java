package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private User user;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("dima@gmail.com");
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(any())).thenReturn("1234");
        Mockito.when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "User can't be null");
        Assertions.assertEquals(actual.getEmail(), user.getEmail(),
                String.format("Expect user with email: %s, "
                        + "but was: %s", user.getEmail(), actual.getEmail()));
        Assertions.assertEquals(actual.getPassword(), user.getPassword(),
                String.format("Expect user with password: %s, "
                        + "but was: %s", user.getPassword(), actual.getPassword()));

    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual, "User can't be null");
        Assertions.assertEquals(actual.get().getId(), user.getId(),
                String.format("Expect user with id: %s, "
                        + "but was: %s", user.getId(), actual.get().getId()));
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual, "User can't be null");
        Assertions.assertEquals(actual.get().getEmail(), user.getEmail(),
                String.format("Expect user with email: %s, "
                        + "but was: %s", user.getEmail(), actual.get().getEmail()));
    }

    @Test
    void findByEmptyEmail_notOk() {
        Assertions.assertEquals(userService.findByEmail(""), Optional.empty(),
                "Expect exception");
    }
}
