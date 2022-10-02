package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static final String EMAIL = "user@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "FAILPASSWORD";
    private UserService userService;
    private RoleService roleService;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches(INVALID_PASSWORD,
                user.getPassword())).thenReturn(true);
        AuthenticationException thrown = Assertions
                .assertThrows(AuthenticationException.class,
                        () -> authenticationService.login(EMAIL, PASSWORD),
                        "Excepted to receive AuthenticationException");
        Assertions.assertEquals("Incorrect username or password!!!", thrown.getMessage());
    }
}
