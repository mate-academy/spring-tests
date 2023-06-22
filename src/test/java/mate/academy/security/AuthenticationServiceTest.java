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

class AuthenticationServiceTest {
    private static final String LOGIN = "john@ttt.com";
    private static final String PASSWORD = "1234";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static User user;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
        user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(LOGIN);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, user);
    }

    @Test
    void login_OK() throws AuthenticationException {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches("1234", PASSWORD))
                .thenReturn(true);
        User actual = authenticationService.login(LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
    }

    @Test
    void login_NotOK() {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches("1234", PASSWORD))
                .thenReturn(true);
        String login = "alice@gmail.com";
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(login, PASSWORD));
    }
}
