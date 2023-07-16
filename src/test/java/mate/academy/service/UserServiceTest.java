package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "123456";
    private final UserDao userDao = mock(UserDao.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService =
            new UserServiceImpl(userDao, passwordEncoder);
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        when(userDao.save(user)).thenReturn(user);

        User actual = userService.save(user);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Long id = user.getId();
        userService.save(user);

        when(userDao.findById(id)).thenReturn(Optional.ofNullable(user));
        Optional<User> byId = userService.findById(id);

        assertTrue(byId.isPresent());
        assertEquals(Optional.ofNullable(user), byId);
    }

    @Test
    void findById_NotOk() {
        Long id = user.getId();
        userService.save(user);

        when(userDao.findById(id))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byId = userService.findById(6L);

        assertFalse(byId.isPresent());
        assertEquals(Optional.empty(), byId);
    }

    @Test
    void findByEmail_Ok() {
        String email = user.getEmail();
        userService.save(user);

        when(userDao.findByEmail(email))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byEmail = userService.findByEmail(email);

        assertTrue(byEmail.isPresent());
        assertEquals(Optional.ofNullable(user), byEmail);
    }

    @Test
    void findByEmail_NotOk() {
        String email = user.getEmail();
        userService.save(user);

        when(userDao.findByEmail(email))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byEmail = userService.findByEmail("alice");

        assertFalse(byEmail.isPresent());
        assertEquals(Optional.empty(), byEmail);
    }
}
