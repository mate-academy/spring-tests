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
    private static final String USER_EMAIL = "john@me.com";
    private static final String USER_PASSWORD = "12345678";
    private User user;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);

        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(
                user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(USER_EMAIL))
                                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(USER_PASSWORD, user.getPassword()))
                                .thenReturn(true);
        User actual = authenticationService.login(
                user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_invalidLogin_notOk() {
        Mockito.when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(USER_PASSWORD, user.getPassword()))
                .thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(
                        user.getEmail(), user.getPassword()),
                "Expected to receive AuthenticationException");
    }

    @Test
    void login_invalidPassword_notOk() {
        Mockito.when(userService.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(USER_PASSWORD, user.getPassword()))
                .thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(
                        user.getEmail(), user.getPassword()),
                "Expected to receive AuthenticationException");
    }
}
