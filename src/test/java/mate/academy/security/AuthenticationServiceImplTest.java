package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "123456";
    private final UserService userService = mock(UserService.class);
    private final RoleService roleService = mock(RoleService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl(
            userService, roleService, passwordEncoder);
    private User expected;

    @BeforeEach
    void setUp() {
        expected = new User();
        expected.setEmail(VALID_EMAIL);
        expected.setPassword(VALID_PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        when(userService.save(any())).thenReturn(expected);
        when(roleService.getRoleByName(any()))
                .thenReturn(new Role(Role.RoleName.USER));

        User actual = authenticationService.register(
                VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        when(userService.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.ofNullable(expected));
        when(passwordEncoder.matches(expected.getPassword(), VALID_PASSWORD))
                .thenReturn(true);

        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void login_wrongData_Ok() {
        when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        try {
            authenticationService.login("alice@i.ua", "123124214");
        } catch (AuthenticationException e) {
            assertEquals(
                    "Incorrect username or password!!!", e.getMessage());
            return;
        }

        fail("Expected to receive Authentication Exception ");
    }
}
