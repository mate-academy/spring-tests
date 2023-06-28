package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private User user;
    private Role role;

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        role = new Role(ID, USER_ROLE);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        when(userDao.save(any())).thenReturn(new User(ID, EMAIL, PASSWORD, Set.of(role)));

        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        user.setId(ID);
        Optional<User> expected = Optional.of(user);
        when(userDao.findByEmail(EMAIL)).thenReturn(expected);

        Optional<User> actual = userService.findByEmail(EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.orElseThrow().getEmail());
        assertEquals(PASSWORD, actual.orElseThrow().getPassword());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> actual = userService.findByEmail(EMAIL);
        assertFalse(actual.isPresent());
    }

    @Test
    void findById_ok() {
        user.setId(ID);
        when(userDao.findById(ID)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(ID);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_invalidId_notOk() {
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> actual = userService.findById(ID);
        assertFalse(actual.isPresent());
    }
}
