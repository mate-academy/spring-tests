package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

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
    private static final String EMAIL = "user@i.ua";
    private static final String PASSWORD = "2345";
    private static final String ENCODED_PASSWORD = "1234";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    private UserDao userDao;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expected = new User();
        expected.setId(1L);
        expected.setEmail(EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(ROLES);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        User actual = userService.save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(anyLong())).thenReturn(Optional.of(expected));
        User actual = userService.findById(expected.getId()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findById_NoSuchId_NotOk() {
        Mockito.when(userDao.findById(anyLong())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(5L);
        assertTrue(userOptional.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(anyString())).thenReturn(Optional.of(expected));
        User actual = userService.findByEmail(expected.getEmail()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_NoSuchEmail_NotOk() {
        Mockito.when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(EMAIL);
        assertTrue(userOptional.isEmpty());
    }
}
