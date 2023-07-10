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
    private static final String PASSWORD = "1234";
    private static final String EMPTY_PASSWORD = "";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_ok() {
        User user = getUser(EMAIL, PASSWORD);
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(Mockito.any()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_ok() {
        User user = getUser(EMAIL, PASSWORD);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        try {
            Optional<User> actual = Optional.ofNullable(
                    authenticationService.login(EMAIL, PASSWORD));
            Assertions.assertFalse(actual.isEmpty());
            Assertions.assertEquals(EMAIL, actual.get().getEmail());
            Assertions.assertEquals(PASSWORD, actual.get().getPassword());
            return;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        Assertions.fail("Login must be successful");
    }

    @Test
    void login_badCredentials_notOk() {
        Assertions.assertThrows(AuthenticationException.class, () -> {
            User user = getUser(EMAIL, EMPTY_PASSWORD);
            Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
            Mockito.when(passwordEncoder.matches(user.getPassword(),
                    user.getPassword())).thenReturn(false);
            authenticationService.login(EMAIL, EMPTY_PASSWORD);
        });
    }

    private User getUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
