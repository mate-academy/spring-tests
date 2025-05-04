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
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "0123456789";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.save(testUser)).thenReturn(testUser);
        User saveUser = userService.save(testUser);
        Assertions.assertEquals(saveUser.getEmail(),EMAIL);
    }

    @Test
    void findByEmail_Ok() {
        testUser.setId(1L);
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        User userFromDb = userService.findByEmail(EMAIL).get();
        Assertions.assertEquals(userFromDb.getId(),1L);
    }

    @Test
    void findById_Ok() {
        testUser.setId(1L);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(testUser));
        User userFromDb = userService.findById(1L).get();
        Assertions.assertEquals(userFromDb.getEmail(),EMAIL);
    }
}
