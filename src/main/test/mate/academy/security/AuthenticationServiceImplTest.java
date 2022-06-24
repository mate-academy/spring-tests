package mate.academy.security;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.source.tree.ModuleTree;
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
    }

    @Test
    void register_validCredential_Ok() {
        String email = "email@email.ok";
        String password = "12345678";
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(expectedUser);

        User actualUser = authenticationService.register(email, password);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void login_validLoginAndPassword_Ok() {
        String email = "email@email.ok";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        Mockito.when(passwordEncoder.matches(password, password)).thenReturn(true);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User actualUser = null;
        try {
            actualUser = authenticationService.login(email, password);
        } catch (AuthenticationException e) {
            fail("Test failed cause: " + e.getMessage());
        }
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void login_invalidCredentials_UserNotFound() {
        String invalidEmail = "invalidEmail";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(invalidEmail);
        expectedUser.setPassword(password);
        Mockito.when(passwordEncoder.matches(password, password)).thenReturn(true);
        Mockito.when(userService.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        User actualUser = null;
        try {
            actualUser = authenticationService.login(invalidEmail, password);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Test failed cause AuthenticationException was not be thrown");
    }
}