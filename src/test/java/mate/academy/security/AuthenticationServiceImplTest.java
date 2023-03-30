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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String INVALID_PASSWORD = "1111";
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void login_withInvalidEmail_notOk() {
        Mockito.when(userService.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        try {
            authenticationService.login(INVALID_EMAIL, VALID_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to recieve AuthenticationException");
    }

    @Test
    void login_withInvalidPassword_notOk() {
        Mockito.when(userService.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(INVALID_PASSWORD,
                user.getPassword())).thenReturn(false);
        try {
            authenticationService.login(VALID_EMAIL, INVALID_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to recieve AuthenticationException");
    }
}
