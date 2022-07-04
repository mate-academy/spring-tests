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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        String email = "testemail@gmail.com";
        String password = "password";
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));

        Mockito.when(roleService.getRoleByName(role.getRoleName().name())).thenReturn(role);
        Mockito.when(userService.save(any())).thenReturn(user);

        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
        Assertions.assertEquals(actual.getRoles(), Set.of(role));
    }

    @Test
    void login_ok() throws AuthenticationException {
        String email = "testemail@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userService.findByEmail(email)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(password, optionalUser.get().getPassword())).thenReturn(true);

        User actual = authenticationService.login(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void login_wrongPassword_notOk() {
        String email = "testemail@gmail.com";
        String password = "password";
        String wrongPassword = "1234";
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userService.findByEmail(email)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(wrongPassword, optionalUser.get().getPassword())).thenReturn(false);

        try {
            authenticationService.login(email, wrongPassword);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_wrongEmail_notOk() {
        String password = "password";
        String wrongEmail = "wrong@gmail.com";

        Mockito.when(userService.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);

        try {
            authenticationService.login(wrongEmail, password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_nullEmail_notOk() {
        String password = "password";
        String nullEmail = null;

        Mockito.when(userService.findByEmail(nullEmail)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);

        try {
            authenticationService.login(nullEmail, password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_nullPassword_notOk() {
        String email = "testemail@gmail.com";
        String password = "password";
        String nullPassword = null;
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userService.findByEmail(email)).thenReturn(optionalUser);
        Mockito.when(passwordEncoder.matches(nullPassword, optionalUser.get().getPassword())).thenReturn(false);

        try {
            authenticationService.login(email, nullPassword);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}