package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "NonExistingEmail";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void save_ok() {
        when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_ok() {
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_invalidId_ok() {
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(2L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_ok() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        assertTrue(actual.isEmpty());
    }
}
