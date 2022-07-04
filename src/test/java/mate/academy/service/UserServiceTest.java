package mate.academy.service;

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
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345678");
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(bob.getPassword())).thenReturn(bob.getPassword());
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(bob.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(bob.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }
}
