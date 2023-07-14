package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_PASSWORD = "abcd";
    private static final String PASSWORD = "1234";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void register_ok() {
        when(userService.save(any())).thenReturn(user);
        when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        Optional<User> actual = Optional.ofNullable(assertDoesNotThrow(() ->
                authenticationService.login(EMAIL, PASSWORD)));
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void login_invalidCredentials_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, INVALID_PASSWORD);
        }, "Incorrect username or password!!!");
    }

}
