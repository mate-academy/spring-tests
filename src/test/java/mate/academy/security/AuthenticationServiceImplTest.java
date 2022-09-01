package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        User expected = new User();
        expected.setId(1L);
        expected.setEmail("harrington@.ua");
        expected.setPassword("12345678");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(expected);
        User actual = authenticationService.register(expected.getEmail(),
                expected.getPassword());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void login_ok() {
        User expected = new User();
        expected.setId(1L);
        expected.setEmail("harrington@.ua");
        expected.setPassword("12345678");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(expected.getEmail()))
                .thenReturn(Optional.of(expected));
        Mockito.when(passwordEncoder.matches(expected.getPassword(), expected.getPassword()))
                .thenReturn(true);
        User actual;
        try {
            actual = authenticationService.login(expected.getEmail(), expected.getPassword());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Incorrect username or password!!!", e);
        }
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void login_emptyUser_notOk() {
        User user = new User();
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(user.getEmail(), user.getPassword()),
                "Should receive AuthenticationException");
    }

    @Test
    void login_notExistingLogin_notOk() {
        User user = new User();
        user.setEmail("harrington@.ua");
        user.setPassword("12345678");
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(user.getEmail(), user.getPassword()),
                "Should receive AuthenticationException");
    }

    @Test
    void login_wrongPassword_notOk() {
        User user = new User();
        user.setEmail("harrington@.ua");
        user.setPassword("12345678");
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("12345", user.getPassword()))
                .thenReturn(false);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(user.getEmail(), user.getPassword()),
                "Should receive AuthenticationException");
    }
}
