package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String ROLE_USER_NAME = "USER";
    private static final String USER_EMAIL = "test@com.ua";
    private static final String USER_PASSWORD = "password";
    private UserService userService;
    private RoleService roleService;
    private AuthenticationService authenticationService;
    private Role userRole;
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        testUser = new User();
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        testUser.setRoles(Set.of(userRole));
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        when(roleService.getRoleByName(ROLE_USER_NAME)).thenReturn(userRole);
        when(userService.save(any())).thenReturn(testUser);
        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getEmail());
        assertEquals(USER_PASSWORD, actual.getPassword());
        assertEquals(Set.of(userRole), actual.getRoles());
    }

    @Test
    void login_Ok() {
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(USER_PASSWORD, testUser.getPassword()))
                .thenReturn(true);
        User actual = null;
        try {
            actual = authenticationService.login(testUser.getEmail(), testUser.getPassword());
        } catch (AuthenticationException e) {
            assertEquals(e.getMessage(), "Incorrect username or password!!!");
        }
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getEmail());
        assertEquals(USER_PASSWORD, actual.getPassword());
        assertEquals(Set.of(userRole), actual.getRoles());
    }
}
