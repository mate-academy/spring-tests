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
    private User bob;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setId(1L);
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(any())).thenReturn("1234");
        Mockito.when(userDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.getEmail(), bob.getEmail(),
                String.format("Should return user with email: %s, "
                        + "but was: %s", bob.getEmail(), actual.getEmail()));
        Assertions.assertEquals(actual.getPassword(), bob.getPassword(),
                String.format("Should return user with password: %s, "
                        + "but was: %s", bob.getPassword(), actual.getPassword()));
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.ofNullable(bob));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.get().getId(), bob.getId(),
                String.format("Should return user with id: %s, "
                        + "but was: %s", bob.getId(), actual.get().getId()));
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.ofNullable(bob));
        Optional<User> actual = userService.findByEmail(bob.getEmail());
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(actual.get().getEmail(), bob.getEmail(),
                String.format("Should return user with email: %s, "
                        + "but was: %s", bob.getEmail(), actual.get().getEmail()));
    }

    @Test
    void findByEmail_emailEmpty_notOk() {
        Assertions.assertEquals(userService.findByEmail(""), Optional.empty(),
                "Shouldn`t throw exception and should return Optional.empty()");
    }

    @Test
    void findByEmail_emailNull_notOk() {
        Assertions.assertEquals(userService.findByEmail(null), Optional.empty(),
                "Shouldn`t throw exception and should return Optional.empty()");
    }

    @Test
    void findByEmail_emailIsAbsent_notOk() {
        Assertions.assertEquals(userService.findByEmail("alice@i.ua"), Optional.empty(),
                "Shouldn`t throw exception and should return Optional.empty()");
    }
}
