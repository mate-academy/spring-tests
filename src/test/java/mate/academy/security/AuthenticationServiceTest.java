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

public class AuthenticationServiceTest {
    private static final String LOGIN = "bob";
    private static final String PASSWORD = "1234";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    private User bob;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        bob = new User();
        bob.setPassword(PASSWORD);
        bob.setEmail(LOGIN);
        bob.setRoles(ROLES);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(bob);
        User registeredBob = authenticationService.register(LOGIN, PASSWORD);
        Assertions.assertNotNull(registeredBob);
        Assertions.assertEquals(registeredBob, bob);
    }

    @Test
    void login_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(bob);
        User newBob = authenticationService.register(LOGIN, PASSWORD);
        Assertions.assertNotNull(newBob);
        Assertions.assertEquals(bob, newBob);
    }

    @Test
    void login_NotOk() {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.empty());
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(LOGIN, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", exception.getMessage());
    }
}
