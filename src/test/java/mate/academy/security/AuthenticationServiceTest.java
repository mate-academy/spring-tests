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
    private static final String EMAIL = "tom@gmail.com";
    private static final String PASSWORD = "1234";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = getUserForTest();
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User register = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(user.getEmail(), register.getEmail());
        Assertions.assertEquals(user.getPassword(), register.getPassword());
        Assertions.assertEquals(user.getRoles(), register.getRoles());
    }

    @Test
    void login_Authentication_Ok() {
        User user = getUserForTest();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail("Not expected to receive AuthenticationException");
        }
    }

    @Test
    void login_WrongLogin_NotOk() {
        User user = getUserForTest();
        Mockito.when(userService.findByEmail("alice@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        AuthenticationException auth = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", auth.getMessage());
    }

    @Test
    void login_WrongPassword_NotOk() {
        User user = getUserForTest();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);
        AuthenticationException auth = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", auth.getMessage());
    }

    private User getUserForTest() {
        User testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return testUser;
    }
}
