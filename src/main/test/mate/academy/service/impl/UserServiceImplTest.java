package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "larry@gmail.com";
    private static final String PASSWORD = "12345678";
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private User user;

    @BeforeEach
    public void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = testUser();
    }

    @Test
    public void save_Ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD);
        Mockito.when(userDao.save(user))
                .thenReturn(user);
        User saveUser = userService.save(user);
        assertEquals(saveUser.getEmail(), EMAIL);
    }

    @Test
    public void findById_Ok() {
        Mockito.when(userDao.findById(ID))
                .thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findById(ID);
        assertFalse(actualOptional.isEmpty());
        User actual = actualOptional.get();
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_NoIdInDB_NotOk() {
        Mockito.when(userDao.findById(ID))
                .thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(ID);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_NoEmailInDB_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL))
                .thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    private User testUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        return user;
    }
}