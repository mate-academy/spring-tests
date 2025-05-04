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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static final String EMAIL = "user@gmail.com";
    private static final String CORRECT_PASSWORD = "12345678";
    private static final String WRONG_PASSWORD = "87654321";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(CORRECT_PASSWORD);

        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, CORRECT_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(CORRECT_PASSWORD, actual.getPassword());
    }

    @Test
    void register_userAlreadyExists_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Assertions.assertThrows(RuntimeException.class,
                () -> authenticationService.register(EMAIL, CORRECT_PASSWORD));
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(CORRECT_PASSWORD, CORRECT_PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(EMAIL, CORRECT_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(CORRECT_PASSWORD, actual.getPassword());
    }

    @Test
    void login_wrongPassword_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(CORRECT_PASSWORD, WRONG_PASSWORD)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, WRONG_PASSWORD));
    }
}
