package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private User user;
    private Role role;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        user = new User();
        role = new Role(Role.RoleName.USER);
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(role));
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(role);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(TEST_EMAIL_OK, TEST_PASSWORD_OK);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL_OK)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        User actual = authenticationService.login(TEST_EMAIL_OK, TEST_PASSWORD_OK);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_not_Ok() {
        Mockito.when(userService.findByEmail(TEST_EMAIL_OK)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(TEST_EMAIL_OK, TEST_PASSWORD_OK));
    }
}
