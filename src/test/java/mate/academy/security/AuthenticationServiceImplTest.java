package mate.academy.security;

import java.util.Optional;
import java.util.Set;
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
    private String email;
    private String password;
    private Role role;
    private User userIn;
    private User userOut;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private UserService userService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        role = new Role(Role.RoleName.USER);
        email = "bob@i.ua";
        password = "12345678";
        userIn = new User();
        userIn.setEmail(email);
        userIn.setPassword(password);
        userIn.setRoles(Set.of(role));
        userOut = new User();
        userOut.setId(1L);
        userOut.setEmail(email);
        userOut.setPassword(passwordEncoder.encode(password));
        userOut.setRoles(Set.of(role));

    }

    @Test
    void register_OK() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(userOut);
        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, userOut);
    }

    @Test
    void login_OK() {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(userOut));
        try {
            User actual = authenticationService.login(email, password);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(actual, userOut);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("An exception was thrown!", e.getMessage());
        }
    }

    @Test
    void login_incorrect_Exception() {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(userOut));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(email, ""),
                "Incorrect username or password!!!");
    }
}
