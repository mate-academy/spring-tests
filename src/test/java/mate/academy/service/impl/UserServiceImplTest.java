package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final long ID = 1L;
    private static final long NOT_VALID_ID = 0L;
    private static final String PASSWORD = "12345";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String EMAIL = "test@ukr.net";
    private static final String NOT_VALID_EMAIL = " ";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User expected;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expected = new User();
        expected.setRoles(ROLES);
        expected.setEmail(EMAIL);
        expected.setPassword(PASSWORD);
        expected.setId(ID);
    }

    @Test
    void save_validUser_ok() {
        User user = new User();
        user.setRoles(ROLES);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        expected.setPassword(ENCODED_PASSWORD);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userDao.save(user)).thenReturn(expected);
        User actual = userService.save(user);
        assertNotNull(actual,
                "method should not return null with valid User");
        assertEquals(ID, actual.getId(),
                "method should return User with actual id");
        assertEquals(EMAIL, actual.getEmail(),
                "method should return User with actual email");
        assertEquals(ENCODED_PASSWORD, actual.getPassword(),
                "method should return User with actual password");
        assertEquals(ROLES, actual.getRoles(),
                "method should return User with actual roles");
    }

    @Test
    void findById_validId_ok() {
        when(userDao.findById(ID)).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(ID);
        assertNotNull(actual,
                "method should not return null if User id is valid");
        assertTrue(actual.isPresent(),
                "method should not return empty Optional if User id is valid");
        assertEquals(ID, actual.get().getId(),
                "method should return User with actual id");
        assertEquals(EMAIL, actual.get().getEmail(),
                "method should return User with actual email");
        assertEquals(PASSWORD, actual.get().getPassword(),
                "method should return User with actual password");
        assertEquals(ROLES, actual.get().getRoles(),
                "method should return User with actual roles");
    }

    @Test
    void findById_notValidId_notOk() {
        when(userDao.findById(NOT_VALID_ID)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(NOT_VALID_ID);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional if User id is not valid");
    }

    @Test
    void findByEmail_validEmail_ok() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertNotNull(actual, "method should not return null if User email id is valid");
        assertTrue(actual.isPresent(),
                "method should not return empty Optional if User email id is valid");
        assertEquals(ID, actual.get().getId(),
                "method should return User with actual id");
        assertEquals(EMAIL, actual.get().getEmail(),
                "method should return User with actual email");
        assertEquals(PASSWORD, actual.get().getPassword(),
                "method should return User with actual password");
        assertEquals(ROLES, actual.get().getRoles(),
                "method should return User with actual roles");
    }

    @Test
    void findByEmail_notValidEmail_notOk() {
        when(userDao.findByEmail(NOT_VALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(NOT_VALID_EMAIL);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional if User email is not valid");
    }
}
