package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertNotNull(actual,
                "Method should return saved user '%s'"
                        .formatted(savedUser));
        assertEquals(savedUser, actual);

    }

    @Test
    void findById_existingUser_ok() {
        when(userDao.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findById(savedUser.getId());
        assertTrue(optionalUser.isPresent(),
                "Method should return optional user '%s' for id '%s'"
                        .formatted(savedUser, savedUser.getId()));
        assertEquals(savedUser, optionalUser.get());
    }

    @Test
    void findById_notExistedUser_notOk() {
        long notExistingId = 100L;
        lenient().when(userDao.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findById(notExistingId);
        assertFalse(optionalUser.isPresent(),
                "Method should return optional empty for id '%s'"
                        .formatted(notExistingId));
    }

    @Test
    void findByEmail_existingEmail_ok() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findByEmail(EMAIL);
        assertTrue(optionalUser.isPresent(),
                "Method should return optional user '%s' for email '%s'"
                        .formatted(savedUser, EMAIL));
        assertEquals(savedUser, optionalUser.get());
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        String notExistingEmail = "notExist@i.ua";
        lenient().when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Optional<User> optionalUser = userService.findByEmail(notExistingEmail);
        assertFalse(optionalUser.isPresent(),
                "Method should return optional empty for email '%s'"
                        .formatted(notExistingEmail));
    }
}
