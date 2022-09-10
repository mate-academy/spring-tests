package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String EMAIL = "user@domain.com";
    private static final String PASSWORD = "12345678";
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDaoImpl.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        role = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(role));
    }

    @Test
    public void save_Ok() {
        User newUser = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        Mockito.when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
        Mockito.when(userDao.save(any())).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    public void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(1L);
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    public void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }
}
