package mate.academy.security;

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

class AuthenticationServiceImplTest {
    private static final String EMAIL = "user@i.ua";
    private static final String PASSWORD = "2345";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private AuthenticationService authenticationService;
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(ROLE.getRoleName().name())).thenReturn(ROLE);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(PASSWORD,
                optionalUser.get().getPassword())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_WrongEmail_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_WrongPassword_NotOk() {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(PASSWORD,
                optionalUser.get().getPassword())).thenReturn(false);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_NullEmail_NotOk() {
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(null, PASSWORD));
    }

    @Test
    void login_NullPassword_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, null));
    }
}
