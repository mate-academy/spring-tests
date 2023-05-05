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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String CORRECT_EMAIL = "bob@i.ua";
    private static final String FAKE_EMAIL = "fake@i.ua";
    private static final String PASSWORD = "1234";
    private static final String FAKE_PASSWORD = "123456";
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(CORRECT_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(CORRECT_EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD,user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(CORRECT_EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_EmailIncorrect_NotOk() {
        Mockito.when(userService.findByEmail(FAKE_EMAIL)).thenReturn(Optional.empty());
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(FAKE_EMAIL, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", exception.getMessage());
    }

    @Test
    void login_PasswordIncorrect_NotOk() {
        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(FAKE_PASSWORD, user.getPassword())).thenReturn(false);
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(CORRECT_EMAIL, FAKE_PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", exception.getMessage());
    }
}
