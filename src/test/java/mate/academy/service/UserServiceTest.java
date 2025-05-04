package mate.academy.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "bob@i.ua";
    private static final String NOT_EXISTED_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private User user;
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void save_user_ok() {
        when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_userWithId_ok() {
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        assertFalse(actual.isEmpty());
        assertEquals(ID, actual.get().getId());
    }

    @Test
    void findById_notExistedUserWithId_notOk() {
        Optional<User> actual = userService.findById(10L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_validEmail_ok() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertFalse(actual.isEmpty());
        assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_notExistedEmail_notOk() {
        Optional<User> actual = userService.findByEmail(NOT_EXISTED_EMAIL);
        assertTrue(actual.isEmpty());
    }
}

