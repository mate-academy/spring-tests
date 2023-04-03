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

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "email@test.test";
    private static final String TEST_PASSWORD = "qwerty123";
    private static User user;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService,
                roleService,
                passwordEncoder
        );
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(TEST_PASSWORD);
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenAnswer(invocation
                -> invocation.getArgument(0));
        User actual = authenticationService.register(VALID_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual, "User can't be null");
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = null;
        actual = authenticationService.login(VALID_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_notOk() {
        User actual = null;
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("non_valid_email@test.test", TEST_PASSWORD));
    }
}
