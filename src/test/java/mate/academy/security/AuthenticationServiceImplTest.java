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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "bobTheBest";
    private static final String USER_ROLE = "USER";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
    }

    @Test
    void loginMethod_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual, "User after login must be to null!");
        Assertions.assertEquals(EMAIL, actual.getEmail(), "Email is not correct!");
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, actual.getPassword()),
                "Password is not correct or encoding done wrong!");
        Assertions.assertEquals(actual, user, "Logged user is incorrect");
    }

    @Test
    void registerMethod_Ok() {
        Mockito.when(roleService.getRoleByName(USER_ROLE)).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual, "User after registration must be to null!!!");
        Assertions.assertEquals(EMAIL, actual.getEmail(), "Email is not correct!");
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, actual.getPassword()),
                "Password is not correct or encoding done wrong!");
        Assertions.assertEquals(actual, user, "Registered user is incorrect");
    }

    @Test
    void loginMethodNot_Ok() {
        try {
            authenticationService.login("bob@gmail.com", "bobNotTheBest");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Login method with incorrect credentials must throw AuthenticationException");
    }
}
