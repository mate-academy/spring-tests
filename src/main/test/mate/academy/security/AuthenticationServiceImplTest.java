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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final String PASSWORD = "qwerty";
    private static final String WRONG_PASSWORD = "qwertty";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        expectedUser = new User();
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
        expectedUser.setRoles(Set.of(USER_ROLE));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);
        Mockito.when(userService.save(any())).thenReturn(expectedUser);
    }

    @Test
    void register_ok() throws AuthenticationException {
        User user = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(user.getEmail(), EMAIL);
        Assertions.assertEquals(user.getPassword(), PASSWORD);
        Assertions.assertNotNull(user);
    }

    @Test
    void register_userAlreadyRegistered_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.register(EMAIL, PASSWORD));
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(),
                PASSWORD)).thenReturn(true);
        User actualUser = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void login_wrongEmail_NotOk() {
        Mockito.when(userService.findByEmail(""))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(),
                PASSWORD)).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(WRONG_EMAIL, PASSWORD));
    }

    @Test
    void login_wrongPassword_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(),
                PASSWORD)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, WRONG_PASSWORD));
    }
}
