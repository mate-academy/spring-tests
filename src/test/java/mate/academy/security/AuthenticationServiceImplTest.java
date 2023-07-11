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
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_PASSWORD = "abcd";
    private static final String PASSWORD = "1234";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = createUser();
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(Mockito.any()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_validCredentials_Ok() {
        User user = createUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        try {
            Optional<User> actual = Optional.ofNullable(
                    authenticationService.login(EMAIL, PASSWORD));
            Assertions.assertEquals(EMAIL, actual.get().getEmail());
            Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void login_invalidCredentials_Ok() {
        User user = createUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        try {
            Optional<User> actual = Optional.ofNullable(
                    authenticationService.login(EMAIL, INVALID_PASSWORD));
            Assertions.assertEquals(EMAIL, actual.get().getEmail());
            Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    private User createUser() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        return bob;
    }

}
