package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

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
    private static final String EMAIL = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        try {
            authenticationService.login(user.getEmail(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
