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
    private static final String EMAIL = "bob@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(ROLES);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(bob);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(PASSWORD, Optional.of(bob).get().getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void login_userNotFound_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_passwordsDoNotMatch_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(PASSWORD, Optional.of(bob).get().getPassword()))
                .thenReturn(false);
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD));
    }
}
