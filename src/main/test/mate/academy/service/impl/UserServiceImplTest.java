package mate.academy.service.impl;

import java.util.Collections;
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
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expected = new User();
        expected.setId(1L);
        expected.setEmail("bob@gmail.com");
        expected.setPassword("encoded_password");
        expected.setRoles(Collections.emptySet());
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail("bob@gmail.com");
        user.setPassword("123456789");
        user.setRoles(Collections.emptySet());
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded_password");
        Mockito.when(userDao.save(Mockito.any())).thenReturn(expected);
        User actual = userService.save(user);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(Mockito.any())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail("bob@gmail.com");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
    }
}
