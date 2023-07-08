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

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "123456";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User expected;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);

        expected = new User();
        expected.setEmail(VALID_EMAIL);
        expected.setPassword(VALID_PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(expected);
        Mockito.when(roleService.getRoleByName(any()))
                .thenReturn(new Role(Role.RoleName.USER));

        User actual = authenticationService.register(
                VALID_EMAIL, VALID_PASSWORD);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.ofNullable(expected));
        Mockito.when(passwordEncoder.matches(expected.getPassword(), VALID_PASSWORD))
                .thenReturn(true);

        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_wrongData_Ok() {
        Mockito.when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        try {
            authenticationService.login("alice@i.ua", "123124214");
        } catch (AuthenticationException e) {
            Assertions.assertEquals(
                    "Incorrect username or password!!!", e.getMessage());
            return;
        }

        Assertions.fail("Expected to receive Authentication Exception ");
    }
}
