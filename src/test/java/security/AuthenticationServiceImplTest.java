package security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.RoleServiceImpl;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceImplTest {
    private static final String EMAIL = "user@domain.com";
    private static final String PASSWORD = "12345678";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        roleService = Mockito.mock(RoleServiceImpl.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService,
                        roleService,
                        passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(role));
    }

    @Test
    public void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    public void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    public void login_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
        assertEquals("Incorrect username or password!!!", exception.getMessage());
    }
}
