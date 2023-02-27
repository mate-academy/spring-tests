package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String RAW_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "wrong";
    private static String ENCODED_PASSWORD;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationServiceImpl authenticationService;
    private Role userRole;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        ENCODED_PASSWORD = passwordEncoder.encode(RAW_PASSWORD);
    }

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(RAW_PASSWORD);
        user.setRoles(Set.of(userRole));
        user.setId(1L);
    }

    @Test
    void register_sampleData_ok() {
        when(roleService.getRoleByName("USER")).thenReturn(userRole);
        when(userService.save(any())).thenReturn(user);
        assertEquals(user, authenticationService.register(EMAIL, RAW_PASSWORD));
    }

    @Test
    void login_correctData_ok() throws AuthenticationException {
        user.setPassword(ENCODED_PASSWORD);
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertEquals(user, authenticationService.login(EMAIL, RAW_PASSWORD));
    }

    @Test
    void login_incorrectPassword_notOk() {
        user.setPassword(ENCODED_PASSWORD);
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            authenticationService.login(EMAIL, WRONG_PASSWORD);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
        }
    }
}
