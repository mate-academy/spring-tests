package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@mail.com";
    private static final String WRONG_PASSWORD = "wrong password";
    private static final String PASSWORD = "1234";
    private static final String ROLE_NAME = "USER";
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authService;
    private User testUser;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        authService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testUser.setId(1L);
    }

    @Test
    void register_Ok() {
        when(roleService.getRoleByName(ROLE_NAME))
                .thenReturn(new Role(Role.RoleName.USER));
        when(userService.save(any(User.class))).thenReturn(testUser);
        User actualUser = authService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(testUser, actualUser);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        when(roleService.getRoleByName(ROLE_NAME))
                .thenReturn(new Role(Role.RoleName.USER));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        User actualUser = authService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(testUser, actualUser);
    }

    @Test
    void login_notOk() throws AuthenticationException {
        when(roleService.getRoleByName(ROLE_NAME))
                .thenReturn(new Role(Role.RoleName.USER));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        try {
            User actualUser = authService.login(EMAIL, WRONG_PASSWORD);
        } catch (Exception e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
