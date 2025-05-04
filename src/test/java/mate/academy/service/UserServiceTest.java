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
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail("valid@i.ua");
        user.setPassword("1234");
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
    }
}
