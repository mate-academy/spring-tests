package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String ENCODED_PASSWORD = "%1234@";
    private static final Role ADMIN = new Role(Role.RoleName.ADMIN);
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ADMIN));
    }

    @Test
    void save_user_OK() {
        Mockito.when(passwordEncoder.encode(any())).thenReturn(ENCODED_PASSWORD);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Mockito.when(userDao.save(any())).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("%1234@", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }

    @Test
    void findById() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(user));
        User actual = userService.findById(1L).orElseThrow(
                () -> new RuntimeException("Couldn't find user by id"));
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }

    @Test
    void findByEmail() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(EMAIL).orElseThrow(
                () -> new RuntimeException("Couldn't find user by email"));
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("ADMIN", actual.getRoles().stream()
                .findFirst().orElseThrow(
                        () -> new RuntimeException("Couldn't find expected user role")
                ).getRoleName().name());
    }
}
