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

class AuthenticationServiceImplTest {
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        RoleService roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
    }

    @Test
    void register_Ok() {
        User user = createUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() {
        User user = createUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            Optional<User> actual = Optional.ofNullable(
                    authenticationService.login(EMAIL, PASSWORD));
            Assertions.assertFalse(actual.isEmpty());
            Assertions.assertEquals(EMAIL, actual.get().getEmail());
            Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void login_invalidCredentials_notOk() {
        User user = createUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            authenticationService.login(EMAIL, "11111");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("login() is expected to throw AuthenticationException");
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        return user;
    }
}
