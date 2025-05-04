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
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final Role ADMIN_ROLE = new Role(Role.RoleName.ADMIN);
    private static final String DEFAULT_EMAIL = "mark@test.ua";
    private static final String DEFAULT_PASSWORD = "12345678";
    private User user;
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void register_defaultUser_ok() {
        Mockito.when(roleService.getRoleByName(USER_ROLE.getRoleName().name()))
                .thenReturn(USER_ROLE);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actualUser = authenticationService.register(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void login_defaultUser_ok() throws AuthenticationException {
        Optional<User> maybeUser = Optional.of(user);
        Mockito.when(userService.findByEmail(DEFAULT_EMAIL)).thenReturn(maybeUser);
        Mockito.when(passwordEncoder.matches(DEFAULT_PASSWORD, maybeUser.get().getPassword()))
                .thenReturn(true);
        User actualUser = authenticationService.login(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void login_emptyUser_notOk() {
        Optional<User> maybeUser = Optional.empty();
        Mockito.when(userService.findByEmail(null)).thenReturn(maybeUser);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(null, DEFAULT_PASSWORD));
    }

    @Test
    void login_nullPassword_notOk() {
        Optional<User> maybeUser = Optional.of(user);
        Mockito.when(userService.findByEmail(DEFAULT_EMAIL)).thenReturn(maybeUser);
        Mockito.when(passwordEncoder.matches(null,
                maybeUser.get().getPassword())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(DEFAULT_EMAIL, null));
    }

    @Test
    void login_invalidEmail_notOk() {
        String wrongEmail = "invalid.email@i.ua";
        Mockito.when(userService.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(wrongEmail, DEFAULT_PASSWORD));
    }

    @Test
    void login_invalidPassword_notOk() {
        String invalidPassword = "invalid password";
        Optional<User> maybeUser = Optional.of(this.user);
        Mockito.when(userService.findByEmail(DEFAULT_EMAIL)).thenReturn(maybeUser);
        Mockito.when(passwordEncoder.matches(invalidPassword, maybeUser.get().getPassword()))
                .thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(DEFAULT_EMAIL, invalidPassword));
    }
}
