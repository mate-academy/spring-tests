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
    private static final String EMAIL = "bob@i.ua";
    private static final String EMAIL_IS_NOT_IN_DB = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static final String REPEAT_PASSWORD_IS_WRONG = "0000";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
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
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    public void login_emailIsNotInDb_Ok() {
        Mockito.when(userService.findByEmail(EMAIL_IS_NOT_IN_DB)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",
                    e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    public void login_passwordIsFalse_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, REPEAT_PASSWORD_IS_WRONG)).thenReturn(false);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",
                    e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    public void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }
}
