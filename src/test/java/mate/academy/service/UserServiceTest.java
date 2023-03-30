package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

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
    private static final String USER_EMAIL = "testuser@gmail.com";
    private static final String USER_PASSWORD = "987654321";
    private static final String USER_PASSWORD_ENCODED = "123456789";
    private User user;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    
    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }
    
    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(USER_PASSWORD_ENCODED);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD_ENCODED, actual.getPassword());
    }
    
    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
    }
    
    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
    }
}
