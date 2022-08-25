package security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceImplTest {
    private static AuthenticationService authenticationService;
    private static final String EMAIL = "alice@mail.com";
    private static final String PASSWORD = "password";
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User expectedUser;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        expectedUser = new User();
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any(User.class))).thenReturn(expectedUser);
        User actualUser = authenticationService.register(EMAIL, PASSWORD);
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    }

    @Test
    void login_Ok() {
        try {
            Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
            Mockito.when(passwordEncoder.matches(expectedUser.getPassword(),
                            expectedUser.getPassword())).thenReturn(true);
            User actualUser = authenticationService.login(EMAIL, PASSWORD);
            assertEquals(expectedUser.getEmail(), actualUser.getEmail());
            assertEquals(expectedUser.getPassword(), actualUser.getPassword());
            assertEquals(expectedUser.getRoles(), actualUser.getRoles());
        } catch (AuthenticationException e) {
            fail("Method should not throw an AuthenticationException if credentials are correct");
        }
    }

    @Test
    void login_wrongEmail_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD),
                "You should throw AuthenticationException if user was not found");
    }

    @Test
    void login_wrongPassword_NotOk() {
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(),
                        expectedUser.getPassword()))
                .thenReturn(false);
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD),
                "You should throw AuthenticationException if password are not equals");
    }
}
