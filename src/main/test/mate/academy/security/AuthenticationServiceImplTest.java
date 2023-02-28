package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void register_validCredential_Ok() {
        String email = "email@email.ok";
        String password = "12345678";
        when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        when(userService.save(Mockito.any(User.class))).thenReturn(expectedUser);

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
        when(passwordEncoder.matches(password, password)).thenReturn(true);
        when(userService.findByEmail(email)).thenReturn(Optional.of(expectedUser));

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
    void login_invalidCredentials_AuthenticationException() {
        String invalidEmail = "invalidEmail";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(invalidEmail);
        expectedUser.setPassword(password);
        when(userService.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        try {
            authenticationService.login(invalidEmail, password);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }

        fail("Test failed cause AuthenticationException was not be thrown");
    }
}
