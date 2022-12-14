package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
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

public class AuthenticationServiceTest {
    private static String BOB_VALID_EMAIL = "bob@mail.com";
    private static String BOB_INVALID_EMAIL = "bb@mail.com";
    private static String SIMPLE_NUMBER_PASSWORD = "1234";
    private static String SIMPLE_LETTER_PASSWORD = "qwerty";
    private static Role userRole;

    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;

    private static User bob;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);

        bob = new User();
        bob.setEmail(BOB_VALID_EMAIL);
        bob.setPassword(SIMPLE_NUMBER_PASSWORD);
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(bob);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);

        User actual = authenticationService.register(BOB_VALID_EMAIL, SIMPLE_NUMBER_PASSWORD);
        Assertions.assertNotNull(actual, "Return value must be not null");
        assertEquals(BOB_VALID_EMAIL, actual.getEmail(), "Email must be equal given value");
        assertEquals(SIMPLE_NUMBER_PASSWORD, actual.getPassword(),
                "Passwords must be equal given value");
    }

    @Test
    void login_Ok() {

        Mockito.when(userService.findByEmail(BOB_VALID_EMAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(bob.getPassword(), SIMPLE_NUMBER_PASSWORD))
                .thenReturn(true);

        try {
            assertEquals(bob, authenticationService.login(BOB_VALID_EMAIL, SIMPLE_NUMBER_PASSWORD),
                    "You must return user object for valid user password and email");
        } catch (AuthenticationException e) {
            Assertions.fail("You don't need throwing exception for valid user");
        }
    }

    @Test
    void login_notOkPassword() {
        Mockito.when(userService.findByEmail(BOB_VALID_EMAIL)).thenReturn(Optional.of(bob));

        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(BOB_VALID_EMAIL, SIMPLE_LETTER_PASSWORD));
    }

    @Test
    void login_notOkLogin() {
        Mockito.when(userService.findByEmail(BOB_VALID_EMAIL)).thenReturn(Optional.of(bob));
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(BOB_INVALID_EMAIL,
                SIMPLE_NUMBER_PASSWORD));
    }
}
