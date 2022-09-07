package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_OK() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any(User.class))).thenReturn(user);
        User actual = authenticationService.register(user.getEmail(), user.getPassword());
        assertNotNull(actual);
        assertNotNull(actual.getEmail());
        assertNotNull(actual.getPassword());
        assertNotNull(actual.getRoles());
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void login_OK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("1234", user.getPassword())).thenReturn(true);
        User actual = null;
        try {
            actual = authenticationService.login(user.getEmail(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.fail("Expected to get a user after login");
        }
        assertNotNull(actual);
        assertNotNull(actual.getEmail());
        assertNotNull(actual.getPassword());
        assertNotNull(actual.getRoles());
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void login_emptyUser_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches("1234", user.getPassword())).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(user.getEmail(), user.getPassword()));
    }

    @Test
    void login_passwordNotMatch_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("1234", user.getPassword())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(user.getEmail(), user.getPassword()));
    }
}
