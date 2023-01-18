package mate.academy.security;

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
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTest {
    private static final long ID = 1L;
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "12345";
    private static final String WRONG_PASSWORD = "123456";
    private static final String USER_ROLE_NAME = "USER";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        user = createUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(ID);
    }

    @Test
    void register_Ok() {
        when(roleService.getRoleByName(USER_ROLE_NAME)).thenReturn(USER_ROLE);
        when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = authenticationService.login(EMAIL, PASSWORD);
        assertEquals(user, actual);
    }

    @Test
    void login_WrongEmail_NotOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_WrongPassword_NotOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, WRONG_PASSWORD));
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        return user;
    }
}
