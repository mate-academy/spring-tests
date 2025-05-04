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
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_validData_ok() {
        String email = "deniks@gmail.com";
        String password = "mypassword";
        Role userRole = new Role(Role.RoleName.USER);

        User expected = new User();
        expected.setEmail(email);
        expected.setPassword(password);
        expected.setId(1L);
        expected.setRoles(Set.of(userRole));

        User registeringUser = new User();
        registeringUser.setPassword(password);
        registeringUser.setEmail(email);
        registeringUser.setRoles(Set.of(userRole));

        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.save(registeringUser)).thenReturn(expected);
        User actual = authenticationService.register(email, password);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_validData_shouldReturnValidUser() {
        String email = "denik@gmail.com";
        String password = "password";
        User expected = new User();
        expected.setEmail(email);
        expected.setPassword(passwordEncoder.encode(password));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(expected));
        User actual = null;
        try {
            actual = authenticationService.login(email, password);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_wrongPassword_shouldThrowException() {
        String email = "denik@gmail.com";
        String password = "password";
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            authenticationService.login(email, "wrongPassword");
        } catch (Exception e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
