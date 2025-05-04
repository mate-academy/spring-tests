package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
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
    private static final Long ID = 1L;
    private static final Long WRONG_ID = 2L;
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "11111111";
    private static final String HASHED_PASSWORD = "oh_here_we_go_again";
    private static final String WRONG_EMAIL = "alice@i,ua";
    private UserService userService;
    private UserDao userDao;
    private User user;
    private Role role;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        userService = new UserServiceImpl(userDao, passwordEncoder);

        role = new Role(ID, Role.RoleName.USER);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn(HASHED_PASSWORD);
        when(userDao.save(user)).thenReturn(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role)));
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(HASHED_PASSWORD, actual.getPassword());
        assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findByEmail_ok() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        User actual = userService.findByEmail(EMAIL).orElseThrow();
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(HASHED_PASSWORD, actual.getPassword());
        assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findByEmail_notOk() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        assertThrows(NoSuchElementException.class, () -> {
            userService.findByEmail(WRONG_EMAIL).orElseThrow();
        }, "NoSuchElementException expected");
    }

    @Test
    void findById_Ok() {
        when(userDao.findById(ID)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        User actual = userService.findById(ID).orElseThrow();
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(HASHED_PASSWORD, actual.getPassword());
        assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findById_NotOk() {
        when(userDao.findById(ID)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        assertThrows(NoSuchElementException.class, () -> {
            userService.findById(WRONG_ID).orElseThrow();
        }, "NoSuchElementException expected");
    }
}
