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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String VALID_EMAIL = "test@g.com";
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_EMAIL = "invalid@mail.com";
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static User testUser;
    private static Role userRole;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        userRole = new Role(Role.RoleName.USER);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        testUser.setRoles(Set.of(userRole));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(testUser)).thenReturn(testUser);
        User actual = userService.save(testUser);
        assertNotNull(actual);
        assertEquals(testUser, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findById(testUser.getId());
        assertTrue(actual.isPresent());
        assertEquals(testUser, actual.get());
    }

    @Test
    void findById_NonExistentId_emptyOptional_Ok() {
        Mockito.when(userDao.findById(testUser.getId())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(testUser.getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findByEmail(VALID_EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(testUser, actual.get());
    }

    @Test
    void findByEmail_invalidEmail_emptyOptional_Ok() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        assertTrue(actual.isEmpty());
    }
}
