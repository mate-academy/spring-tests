package mate.academy.security;

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
import static org.mockito.ArgumentMatchers.any;

public class AuthenticationServiceTest {
    private final static String USER_EMAIL = "bob@gmail.ua";
    private final static String USER_PASSWORD = "1234";
    private final static String ROLE = "USER";
    private final Role userRole = new Role(Role.RoleName.USER);
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder encoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, encoder);
        user = new User();
        user.setId(50L);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(ROLE)).thenReturn(userRole);
        Mockito.when(userService.save(any())).thenReturn(user);
        User registerUser = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(registerUser);
        Assertions.assertEquals(user, registerUser);
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches(USER_PASSWORD, user.getPassword())).thenReturn(true);
        User actualUser = authenticationService.login(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void login_notMatchesPassword_notOk() {
        String expectedMessage = "Incorrect username or password!!!";
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches(USER_PASSWORD, user.getPassword())).thenReturn(false);
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, USER_PASSWORD));
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void login_notExistUserByThisLogin_notOk() {
        String expectedMessage = "Incorrect username or password!!!";
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, USER_PASSWORD));
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
