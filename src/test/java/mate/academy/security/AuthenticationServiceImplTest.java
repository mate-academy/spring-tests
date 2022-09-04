package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        roles = new ArrayList<>();
        roles.add(new Role(Role.RoleName.ADMIN));
        roles.add(new Role(Role.RoleName.USER));
    }

    @Test
    void register_OK() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(roles.get(1));
        user.setRoles(Set.of(roles.get(1)));
        Mockito.when(userService.save(any())).thenReturn(user);
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
        user.setRoles(new HashSet<>(roles));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
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
        assertEquals(2, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
        assertEquals("ADMIN", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("ADMIN"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void login_emptyUser_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        try {
            User actual = authenticationService.login(user.getEmail(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_passwordNotMatch_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(false);
        try {
            User actual = authenticationService.login(user.getEmail(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
