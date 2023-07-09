package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserDao userDao = Mockito.mock(UserDao.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private UserService userService = new UserServiceImpl(userDao, passwordEncoder);
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        user = new User();
        user.setPassword(TEST_PASSWORD_OK);
        user.setEmail(TEST_EMAIL_OK);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    void findById_Not_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                userService.findById(user.getId()).get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_Not_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                userService.findByEmail(user.getEmail()).get());
    }
}
