package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void save_Ok() {
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userDao.findById(1L);
        assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        assertEquals(user.getEmail(), actual.get().getEmail());
    }
}
