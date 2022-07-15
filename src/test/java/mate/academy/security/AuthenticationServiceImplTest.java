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
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "qwe123";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private User user;
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(
                ROLE.getRoleName().name())).thenReturn(ROLE);
        Mockito.when(userService.save(any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(Set.of(ROLE), actual.getRoles());
    }

    @Test
    void login_ok() throws AuthenticationException {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(PASSWORD, optionalUser.get().getPassword()))
                .thenReturn(true);

        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(Set.of(ROLE), actual.getRoles());
    }

    @Test
    void login_nullEmail_notOk() {
        Mockito.when(userService.findByEmail(null)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);

        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(null, PASSWORD));
    }

    @Test
    void login_wrongEmail_notOk() {
        String wrongEmail = "incorrect@gmail.com";
        Mockito.when(userService.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);

        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(wrongEmail, PASSWORD));
    }

    @Test
    void login_nullPassword_notOk() {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(null,
                optionalUser.get().getPassword())).thenReturn(false);

        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, null));
    }

    @Test
    void login_wrongPassword_notOk() {
        String wrongPassword = "wrongPassword";
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(wrongPassword,
                optionalUser.get().getPassword())).thenReturn(false);

        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, wrongPassword));
    }
}
