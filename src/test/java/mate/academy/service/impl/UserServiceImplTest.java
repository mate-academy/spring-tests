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

public class UserServiceImplTest {
    private UserService userService;
    private User user;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail("bchupika@mate.academy");
        user.setPassword("12345678");
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userService.findByEmail("bchupika@mate.academy")).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail("bchupika@mate.academy");
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getEmail(), "bchupika@mate.academy");
        Assertions.assertEquals(actual.get().getPassword(), "12345678");
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        Mockito.when(userDao.findByEmail("wrong@gmail.com")).thenReturn(Optional.ofNullable(null));
        Optional<User> userFromDb = userService.findByEmail("wrong@gmail.com");
        Assertions.assertEquals(Optional.ofNullable(null), userFromDb);
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getEmail(), "bchupika@mate.academy");
        Assertions.assertEquals(actual.get().getPassword(), "12345678");
    }

    @Test
    void findById_invalidId_ok(){
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.ofNullable(null));
        Optional<User> userFromDb = userService.findById(2L);
        Assertions.assertEquals(Optional.ofNullable(null), userFromDb);
    }
}
