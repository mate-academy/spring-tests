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

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@g.com";
    private static final String PASSWORD = "1234567890";
    private static final String INCORRECT_PASSWORD = "7777";

    private static final String USER_ROLE = "USER";
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(USER_ROLE))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void register_userAlreadyExists_notOk() {
        Mockito.when(roleService.getRoleByName(USER_ROLE))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(userService.save(user))
                .thenThrow(new RuntimeException("User with email already exists"));
        Assertions.assertThrows(RuntimeException.class,
                () -> authenticationService.register(EMAIL, PASSWORD));
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertNotNull(actual);
    }

    @Test
    void login_incorrectPassword_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, INCORRECT_PASSWORD)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, INCORRECT_PASSWORD));
    }
}
