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
public class AuthenticationServiceImplTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String INVALID_USER_EMAIL = "ghztxjn";
    private static final String PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "qwerty";

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
                roleService,passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(USER_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(USER_EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = null;
        try {
            actual = authenticationService.login(USER_EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e.getMessage());
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_InvalidEmail_NotOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(INVALID_USER_EMAIL)).thenReturn(Optional.empty());
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(INVALID_USER_EMAIL, INVALID_PASSWORD));
        Assertions.assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void login_InvalidPassword_NotOk() {
        Mockito.when(userService.findByEmail(INVALID_PASSWORD)).thenReturn(Optional.empty());
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(INVALID_USER_EMAIL, INVALID_PASSWORD));
        Assertions.assertEquals("Invalid username or password", exception.getMessage());
    }
}
