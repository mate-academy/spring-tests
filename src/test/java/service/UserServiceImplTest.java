package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        User expected = new User();
        expected.setPassword("alice@mail.com");
        expected.setPassword("password");
        Mockito.when(passwordEncoder.encode(expected.getPassword()))
                .thenReturn(expected.getPassword());
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        User actual = userService.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void save_emptyUser_NotOk() {
        User expected = new User();
        Mockito.when(passwordEncoder.encode(expected.getPassword()))
                .thenReturn(expected.getPassword());
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        assertNull(userService.save(expected).getId());
    }

    @Test
    void findById_Ok() {
        User expected = new User();
        expected.setId(1L);
        Mockito.when(userDao.findById(expected.getId())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(expected.getId(), actual.get().getId());
    }

    @Test
    void findById_idNotExists_NotOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertTrue(userService.findById(1L).isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User expected = new User();
        expected.setEmail("alice@mail.com");
        Mockito.when(userDao.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail(expected.getEmail());
        assertTrue(actual.isPresent());
    }

    @Test
    void findByEmail_emailNotExists_NotOk() {
        String email = "alice@mail.com";
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(email);
        assertTrue(actual.isEmpty());
    }
}
