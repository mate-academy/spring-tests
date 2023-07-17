package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private User user;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        RoleService roleService = mock(RoleService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void register_Ok() {
        when(userService.save(any())).thenReturn(user);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        Optional<User> actual = Optional.ofNullable(assertDoesNotThrow(() ->
                authenticationService.login(EMAIL, PASSWORD)));
        assertFalse(actual.isEmpty());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void login_invalidCredentials_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(EMAIL, "1"),
                "Incorrect username or password!!!");
    }
}
