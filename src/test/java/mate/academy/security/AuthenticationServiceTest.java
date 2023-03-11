package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceTest {
    private static final String EMAIL = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    private User user;
    private User savedUser;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(USER)));
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(EMAIL);
        savedUser.setPassword(passwordEncoder.encode(PASSWORD));
        savedUser.setRoles(Set.of(new Role(USER)));
    }

    @Test
    @Order(1)
    void register_validUser_ok() {
        User actual = registerUser(user);
        assertNotNull(actual,
                "Method should return registered user '%s' for email '%s' and password '%s'"
                        .formatted(savedUser, user.getEmail(), user.getPassword()));
        assertEquals(actual, savedUser,
                "Method should return user '%s' but returned '%s'"
                        .formatted(savedUser, actual));
    }

    @Test
    @Order(2)
    void register_nullEmail_notOk() {
        user.setEmail(null);
        User actual = registerUser(user);
        assertNull(actual,
                "Method should return null with 'null' email");
    }

    @Test
    @Order(3)
    void register_nullPassword_notOk() {
        user.setPassword(null);

        when(roleService.getRoleByName("USER")).thenReturn(new Role(USER));
        lenient().when(userService.save(user)).thenReturn(savedUser);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNull(actual,
                "Method should return null with 'null' password");
    }

    @Test
    @Order(4)
    void login_validEmail_ok() {
        findByEmailMock();
        User actual = assertDoesNotThrow(() -> authenticationService.login(EMAIL, PASSWORD),
                "Method does not throw exception with email '%s' and password '%s'"
                        .formatted(EMAIL, PASSWORD));
        assertNotNull(actual,
                "Method should return user '%s'".formatted(savedUser));
        assertEquals(actual, savedUser,
                "Method should return '%s' but returned '%s'"
                        .formatted(savedUser, actual));
    }

    @Test
    @Order(5)
    void login_notValidEmail_notOk() {
        findByEmailMock();
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.login("notExist@i.ua", PASSWORD));
        assertEquals("Incorrect username or password!!!", exception.getMessage());
    }

    @Test
    @Order(6)
    void login_notValidPassword_notOk() {
        findByEmailMock();
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, "NOT_VALID"));
        assertEquals("Incorrect username or password!!!", exception.getMessage());
    }

    private User registerUser(User user) {
        when(roleService.getRoleByName(USER.name())).thenReturn(new Role(USER));
        lenient().when(userService.save(user)).thenReturn(savedUser);
        return authenticationService.register(EMAIL, PASSWORD);
    }

    private void findByEmailMock() {
        lenient().when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
    }
}
