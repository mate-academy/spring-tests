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
    private User user;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail("bchupika@mate.academy");
        user.setPassword("12345678");
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual,
                "User should not be null after successful registration");
        Assertions.assertEquals(user.getEmail(), actual.getEmail(),
                "User's email should be the same as the one used during registration");
        Assertions.assertEquals(user.getPassword(), actual.getPassword(),
                "User's password should be the same as the one used during registration");
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        try {
            authenticationService.login("wrong@email.com", "123456789");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
