package mate.academy.security;

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
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_EMAIL = "tom@i.ua";
    private static final String INVALID_PASSWORD = "1234567";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, user);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("12345678", VALID_PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, user);
    }

    @Test
    void login_emailInvalid_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches("12345678", VALID_PASSWORD)).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(INVALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    void login_passwordInvalid_notOk() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("12345678", INVALID_PASSWORD)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(VALID_EMAIL, INVALID_PASSWORD));
    }
}
