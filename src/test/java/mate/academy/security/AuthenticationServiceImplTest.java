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

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private User user;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, actual.getPassword()));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.save(user)).thenReturn(user);
        userService.save(user);
        try {
            authenticationService.login("wrong@email.com", "123456789");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
