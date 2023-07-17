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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "invalid@mail";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "0000";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        testUser = new User();
        Role role = new Role(Role.RoleName.USER);
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        testUser.setRoles(Set.of(role));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        assertNotNull(actual);
        assertEquals(testUser, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, testUser.getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        assertNotNull(actual);
        assertEquals(testUser, actual);
    }

    @Test
    void login_invalidEmailAndPassword_authenticationException() {
        try {
            User actual = authenticationService.login(INVALID_EMAIL, INVALID_PASSWORD);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_invalidPassword_authenticationException() {
        try {
            User actual = authenticationService.login(VALID_EMAIL, INVALID_PASSWORD);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected to receive AuthenticationException");
    }
}
