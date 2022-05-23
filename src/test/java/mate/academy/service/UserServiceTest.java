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
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        User user = new User();
        user.setEmail("bob@i.com");
        user.setPassword("1234");
        Mockito.when(userDao.save(user)).thenReturn(user);
        User save = userService.save(user);
        Assertions.assertEquals(user.getPassword(), save.getPassword());
    }

    @Test
    void findById() {
        String email = "bob@i.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setId(1L);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> byIdUser = userService.findById(1L);
        Assertions.assertNotNull(byIdUser);
        Assertions.assertEquals("1234", byIdUser.get().getPassword());
    }

    @Test
    void findByEmail_ok() {
        String email = "bob@i.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> byEmailUser = userService.findByEmail(email);
        Assertions.assertNotNull(byEmailUser);
        Assertions.assertEquals("1234", byEmailUser.get().getPassword());

    }
}