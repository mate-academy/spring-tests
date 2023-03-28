package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final Long ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    private static final String EMAIL = "bob@mate.academy";
    private static final String NOT_EXISTING_EMAIL = "alice@mate.academy";

    private static final String PASSWORD = "12345678";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        bob = new User();
        bob.setId(ID);
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(ROLES);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(bob.getPassword());
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(ID);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
        assertEquals(ROLES, actual.get().getRoles());
    }

    @Test
    void findById_notExistingId_notOk() {
        Optional<User> actual = userService.findById(NOT_EXISTING_ID);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_nullId_notOk() {
        Optional<User> actual = userService.findById(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
        assertEquals(ROLES, actual.get().getRoles());
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        Optional<User> actual = userService.findByEmail(NOT_EXISTING_EMAIL);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        Optional<User> actual = userService.findByEmail(null);
        assertTrue(actual.isEmpty());
    }
}
