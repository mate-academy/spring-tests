package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private String email = "user@gmail.com";
    private String password = "password";
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(USER)));
    }

    @Test
    void register_ValidData_Ok() throws AuthenticationException {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(USER));
        Mockito.when(userService.save(user)).thenReturn(user);
        User actual = authenticationService.register(email, password);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void register_EmailIsNull_NotOk() {
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> {
            authenticationService.register(null, password);
        });
        assertEquals("Email and password can't be null", ex.getMessage());
    }

    @Test
    void register_PasswordIsNull_NotOk() {
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> {
            authenticationService.register(email, null);
        });
        assertEquals("Email and password can't be null", ex.getMessage());
    }

    @Test
    void login_ValidData_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(!passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login(email, password);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_PasswordIsNull_NotOk() throws AuthenticationException {
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(email, null);
        });
        assertEquals("Email and password can't be null", ex.getMessage());

    }

    @Test
    void login_EmailIsNull_NotOk() throws AuthenticationException {
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(null, password);
        });
        assertEquals("Email and password can't be null", ex.getMessage());
    }
}
