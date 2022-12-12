package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
    }

    @Test
    void register_saveNewUser_Ok() {
        String email = "bob@gmail.com";
        String password = "12345678";
        Role role = new Role(Role.RoleName.USER);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        Mockito.when(roleService.getRoleByName(any())).thenReturn(role);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(email, password);
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_Ok() {
        String email = "bob@gmail.com";
        String password = "12345678";
        Role role = new Role(Role.RoleName.USER);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(role));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            User actual = authenticationService.login(email, password);
            assertNotNull(actual);
        } catch (AuthenticationException e) {
            throw new RuntimeException("User not found.", e);
        }
    }

    @Test
    void login_userNotExists_notOk() {
        String email = "bob@gmail.com";
        String password = "12345678";
        try {
            authenticationService.login(email, password);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected - AuthenticationException: Incorrect username or password!!!");
    }

    @Test
    void login_passwordsNotMatch_Ok() {
        String email = "bob@gmail.com";
        String password = "12345678";
        String otherPassword = "some_password";
        Role role = new Role(Role.RoleName.USER);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(role));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            authenticationService.login(email, otherPassword);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected - AuthenticationException: Incorrect username or password!!!");
    }
}
