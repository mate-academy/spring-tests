package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String EMAIL = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    @Mock
    private UserDao userDao;
    private User user;
    private User savedUser;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(EMAIL);
        savedUser.setPassword(passwordEncoder.encode(PASSWORD));
    }

    @Test
    void save_validUser_Ok() {
        when(userDao.save(user)).thenReturn(savedUser);
        User actual = userService.save(user);
        assertEquals(savedUser, actual);

    }

    @Test
    void findById_existedUser_ok() {
        when(userDao.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findById(savedUser.getId());
        assertTrue(optionalUser.isPresent());
        assertEquals(savedUser, optionalUser.get());
    }

    @Test
    void findById_notExistedUser_notOk() {
        lenient().when(userDao.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findById(100L);
        assertFalse(optionalUser.isPresent());
    }

    @Test
    void findByEmail_existedEmail_ok() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findByEmail(EMAIL);
        assertTrue(optionalUser.isPresent());
        assertEquals(savedUser, optionalUser.get());
    }

    @Test
    void findByEmail_notExistedEmail_notOk() {
        lenient().when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findByEmail("notExist@i.ua");
        assertFalse(optionalUser.isPresent());
    }
}
