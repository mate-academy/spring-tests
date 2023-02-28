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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private User user;
    private Optional<User> optionalUser;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role roleUser = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(roleUser));
        optionalUser = Optional.of(user);
        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        RoleService roleService = Mockito.mock(RoleService.class);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(roleUser);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_validData_ok() {
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_validEmail_ok() throws AuthenticationException {
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_notValidEmail_notOk() {
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(false);
        String invalidEmail = "alice@i.ua";
        AuthenticationException ex = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(invalidEmail, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", ex.getMessage());
    }

    @Test
    void login_nullEmail_notOk() {
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(false);
        AuthenticationException ex = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(null, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", ex.getMessage());
    }

    @Test
    void login_notValidPassword_notOk() {
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(false);
        String invalidPassword = "invalidPassword";
        AuthenticationException ex = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, invalidPassword));
        Assertions.assertEquals("Incorrect username or password!!!", ex.getMessage());
    }

    @Test
    void login_nullPassword_notOk() {
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(false);
        AuthenticationException ex = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, null));
        Assertions.assertEquals("Incorrect username or password!!!", ex.getMessage());
    }
}
