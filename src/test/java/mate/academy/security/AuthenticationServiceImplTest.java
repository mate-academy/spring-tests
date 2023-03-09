package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String TEST_EMAIL = "bob@i.ua";
    private static final String TEST_PASSWORD = "1234";
    private static final User TEST_USER = new User();
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void beforeAll() {
        TEST_USER.setPassword(TEST_PASSWORD);
        TEST_USER.setEmail(TEST_EMAIL);
    }

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
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User actual = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual, "Method shouldn't return null");
        Assertions.assertEquals(TEST_EMAIL, actual.getEmail(),
                String.format("Should return user with email: %s, but was: %S",
                        TEST_EMAIL, actual.getEmail()));
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(TEST_USER));
        Mockito.when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertEquals(TEST_USER, actual,
                String.format("Should return: %s, for email: %s, but was: %S",
                        TEST_USER, TEST_EMAIL, actual));
    }

    @Test
    void login_withIncorrectLogin_noOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        });
    }

    @Test
    void login_withIncorrectPassword_noOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(TEST_USER));
        Mockito.when(passwordEncoder.matches(TEST_PASSWORD, "")).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        });
    }

}
