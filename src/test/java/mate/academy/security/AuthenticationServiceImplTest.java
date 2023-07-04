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
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService
                = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);

        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
    }

    @Test
    void register_Ok() {
        String email = "bob@i.ua";
        String password = "12345";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        User actual = authenticationService.register(email, password);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void login_Ok() {
        String email = "bob@i.ua";
        String password = "12345";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            Optional<User> actual =
                    Optional.ofNullable(authenticationService.login(email, password));
            Assertions.assertFalse(actual.isEmpty());
            Assertions.assertEquals(email, actual.get().getEmail());
            Assertions.assertEquals(password, actual.get().getPassword());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

    }

    @Test
    void login_invalidCredentials_notOk() {
        String email = "bob@i.ua";
        String password = "12345";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        try {
            authenticationService.login(email, "11111");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }

        Assertions.fail("Expected to get AuthenticationException");
    }
}
