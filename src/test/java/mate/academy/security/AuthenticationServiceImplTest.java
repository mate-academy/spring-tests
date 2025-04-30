package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private User user;
    private Role role;
    private UserService userService = Mockito.mock(UserService.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private AuthenticationService authenticationService =
            new AuthenticationServiceImpl(userService, roleService, passwordEncoder);

    @BeforeEach
    void setUp() {
        user = new User();
        role = new Role(Role.RoleName.USER);
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(role));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(role);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(TEST_EMAIL_OK, TEST_PASSWORD_OK);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL_OK)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        User actual = authenticationService.login(TEST_EMAIL_OK, TEST_PASSWORD_OK);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_not_Ok() {
        Mockito.when(userService.findByEmail(TEST_EMAIL_OK)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(TEST_EMAIL_OK, TEST_PASSWORD_OK));
    }
}
