package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static String expectedEmail;
    private static String expectedPassword;
    private static User expectedUser;

    @BeforeAll
    static void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expectedEmail = "vitaliy@i.ua";
        expectedPassword = "12345678";
        expectedUser = new User();
        expectedUser.setEmail(expectedEmail);
        expectedUser.setPassword(expectedPassword);
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        Mockito.when(passwordEncoder.encode(expectedPassword)).thenReturn(expectedPassword + "123");
        User actual = userService.save(expectedUser);
        Assertions.assertNotEquals(expectedPassword, actual.getPassword());
        Assertions.assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void findById_ok() {
        long id = 1L;
        expectedUser.setId(id);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(expectedUser));
        User actual = userService.findById(id).get();
        Assertions.assertEquals(id, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(expectedEmail)).thenReturn(Optional.of(expectedUser));
        User actual = userService.findByEmail(expectedEmail).get();
        Assertions.assertEquals(expectedEmail, actual.getEmail());
    }
}
