package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserServiceImplTest {
    private UserServiceImpl userService;

    @Test
    void save() {
        User user = getTestUserWithId();
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("");
        Mockito.when(userDao.save(Mockito.any())).thenReturn(user);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        User savedUser = userService.save(user);
        assertEquals(savedUser.toString(), user.toString());
    }

    @Test
    void findById() {
        User user = getTestUserWithId();
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("");
        Mockito.when(userDao.findById(Mockito.any())).thenReturn(Optional.of(user));
        userService = new UserServiceImpl(userDao, passwordEncoder);
        assertFalse(userService.findById(1L).isEmpty());
    }

    @Test
    void findByEmail() {
        User user = getTestUserWithId();
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("");
        Mockito.when(userDao.findByEmail("email@gmail.com")).thenReturn(Optional.of(user));
        userService = new UserServiceImpl(userDao, passwordEncoder);
        assertFalse(userService.findByEmail("email@gmail.com").isEmpty());
    }

    private User getTestUserWithId() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@gmail.com");
        user.setPassword("12345678secure");
        return user;
    }
}
