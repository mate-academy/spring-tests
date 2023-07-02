package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void findById_ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test123@testmail.net");
        user.setPassword(passwordEncoder.encode("1234"));
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        User actual = userService.findById(1L).get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findById(1L).isEmpty());
    }

    @Test
    void findByEmail_ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test123@testmail.net");
        user.setPassword(passwordEncoder.encode("1234"));
        Mockito.when(userDao.findByEmail("test123@testmail.net"))
                .thenReturn(Optional.of(user));
        User expected = user;
        User actual = userService.findByEmail("test123@testmail.net").get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail("test123@testmail.net"))
                .thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findByEmail("test123@testmail.net").isEmpty());
    }
}
