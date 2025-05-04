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
    private static final String TEST_EMAIL = "bob@i.ua";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_ROLE = "USER";
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
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(TEST_ROLE)).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any(User.class))).thenReturn(user);
        User actual = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_EMAIL, actual.getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.getEmail()));
        Assertions.assertEquals(TEST_PASSWORD, actual.getPassword(),
                String.format("Expected password should be %s, but was %s",
                        TEST_PASSWORD, actual.getPassword()));
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_EMAIL, actual.getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.getEmail()));
        Assertions.assertEquals(TEST_PASSWORD, actual.getPassword(),
                String.format("Expected password should be %s, but was %s",
                        TEST_PASSWORD, actual.getPassword()));
    }

    @Test
    void login_userIsEmpty_notOk() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(TEST_EMAIL, TEST_PASSWORD),
                "Expected AuthenticationException to be thrown");
    }

    @Test
    void login_notEqualPasswords_notOk() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(TEST_EMAIL, TEST_PASSWORD),
                "Expected AuthenticationException to be thrown");
    }
}
