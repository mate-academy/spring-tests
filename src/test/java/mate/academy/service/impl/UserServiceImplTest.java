package mate.academy.service.impl;

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
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
    }

    @Test
    void findById_Ok() {
        Long id = 1L;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findById(id);
        Assertions.assertEquals(user, actualOptional.get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(user, actual.get());
    }
}
