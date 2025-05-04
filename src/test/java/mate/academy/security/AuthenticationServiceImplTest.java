package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private static final String EMAIL = "test@ukr.net";
    private static final String PASSWORD = "12345";
    private static final String ROLE_NAME = "USER";
    private AuthenticationService authenticationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        testRole = new Role(USER);
        testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(testRole));
    }

    @Test
    void register_validData_ok() {
        when(roleService.getRoleByName(ROLE_NAME)).thenReturn(testRole);
        when(userService.save(testUser)).thenReturn(testUser);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual, "method should not return null if  Users valuesis valid");
        assertEquals(EMAIL, actual.getEmail(),
                "method should return User with actual email");
        assertEquals(PASSWORD, actual.getPassword(),
                "method should return User with actual password");
        assertTrue(actual.getRoles().contains(testRole),
                "method should return User with actual roles");
    }

    @Test
    void login_existedUser_ok() throws AuthenticationException {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, testUser.getPassword())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        assertNotNull(actual, "method should not return null if Users values is valid");
        assertEquals(EMAIL, actual.getEmail(),
                "method should return User with actual email");
        assertEquals(PASSWORD, actual.getPassword(),
                "method should return User with actual password");
        assertTrue(actual.getRoles().contains(testRole),
                "method should return User with actual roles");
    }

    @Test
    void login_notValidPassword_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, testUser.getPassword())).thenReturn(false);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD),
                "method should throw AuthenticationException if User password is incorrect");
    }

    @Test
    void login_notValidEmail_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD),
                "method should throw AuthenticationException "
                        + "if email incorrect or absent in database");
    }
}
