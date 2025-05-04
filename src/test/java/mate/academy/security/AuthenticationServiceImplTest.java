package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@bob.ua";
    private static final String INVALID_EMAIL = "not-bob@bob.ua";
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "11";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches("12345678", VALID_PASSWORD))
                .thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_InvalidEmailAndPassword_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches("12345678", VALID_PASSWORD))
                .thenReturn(true);
        AuthenticationException authenticationException =
                assertThrows(AuthenticationException.class,
                        () -> authenticationService.login(INVALID_EMAIL, INVALID_PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!",
                authenticationException.getMessage());
    }
}
