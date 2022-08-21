package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, encoder);
    }

    @Test
    void save_ok() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("11111111");
        Mockito.when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_ok() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("11111111");
        user.setId(1L);
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("11111111");
        user.setId(1L);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(user.getEmail());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }
}
