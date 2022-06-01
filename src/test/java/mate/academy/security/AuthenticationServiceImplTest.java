package mate.academy.security;

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
import static org.mockito.ArgumentMatchers.any;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_OK() {
        User vitalii = new User();
        String email = "vitalii@gmail.com";
        String password = "1234";
        Role vitalisRole = new Role(Role.RoleName.USER);
        vitalii.setEmail(email);
        vitalii.setPassword(password);
        Mockito.when(roleService.getRoleByName(any())).thenReturn(vitalisRole);
        Mockito.when(userService.save(any())).thenReturn(vitalii);
        User actualUser = authenticationService.register(vitalii.getEmail(), vitalii.getPassword());
        Assertions.assertEquals(vitalii.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(vitalii.getPassword(), actualUser.getPassword());
    }

    @Test
    void login_OK() {
        User expectedVitalii = new User();
        String login = "vitalii@gmail.com";
        String password = "1234";
        Mockito.when(userService.findByEmail(expectedVitalii.getEmail()))
                .thenReturn(Optional.of(expectedVitalii));
        Mockito.when(passwordEncoder.matches(expectedVitalii.getPassword(), expectedVitalii.getPassword()))
                .thenReturn(true);
        try {
            User actualVitalii = authenticationService.login(expectedVitalii.getEmail(),
                    expectedVitalii.getPassword());
            Assertions.assertNotNull(actualVitalii, "Actual user can not be null");
            Assertions.assertEquals(expectedVitalii.getEmail(),actualVitalii.getEmail(), "Dude, e-mail is not correct!");
            Assertions.assertEquals(expectedVitalii.getPassword(), actualVitalii.getPassword(),
                    "Dude, you made mistake in password!");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
        }
    }

    @Test
    void login_notExistentEmail_NotOk() {
        User expectedVitalii = new User();
        String login = "vitalii@gmail.com";
        String password = "1234";
        Mockito.when(userService.findByEmail(expectedVitalii.getEmail()))
                .thenReturn(Optional.of(expectedVitalii));
        Mockito.when(passwordEncoder.matches(expectedVitalii.getPassword(), expectedVitalii.getPassword()))
                .thenReturn(true);
        try {
            User actualVitalii = authenticationService.login("vitali@gmail.com", "1234");
            Assertions.assertNotNull(expectedVitalii.getEmail(), actualVitalii.getEmail());
            Assertions.assertEquals(expectedVitalii.getPassword(), actualVitalii.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_NOK_passwordsAreDifferent() {
        User expectedVitalii = new User();
        String login = "vitalii@gmail.com";
        String password = "1234";
        Mockito.when(userService.findByEmail(expectedVitalii.getEmail()))
                .thenReturn(Optional.of(expectedVitalii));
        Mockito.when(passwordEncoder.matches(expectedVitalii.getPassword(), expectedVitalii.getPassword()))
                .thenReturn(true);
        try {
            User actualVitalii = authenticationService.login("vitalii@gmail.com", "12345678");
            Assertions.assertEquals(expectedVitalii.getEmail(), actualVitalii.getEmail());
            Assertions.assertNotEquals(expectedVitalii.getPassword(), actualVitalii.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_NOK_emailAndPassword_wrong() {
        User expectedVitalii = new User();
        String login = "vitalii@gmail.com";
        String password = "1234";
        Mockito.when(userService.findByEmail(expectedVitalii.getEmail()))
                .thenReturn(Optional.of(expectedVitalii));
        Mockito.when(passwordEncoder.matches(expectedVitalii.getPassword(), expectedVitalii.getPassword()))
                .thenReturn(true);
        try {
            User actualNOTVitalii = authenticationService.login("alexa@ukr.net", "87654321");
            Assertions.assertNotEquals(expectedVitalii.getEmail(), actualNOTVitalii.getEmail());
            Assertions.assertNotEquals(expectedVitalii.getPassword(), actualNOTVitalii.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
