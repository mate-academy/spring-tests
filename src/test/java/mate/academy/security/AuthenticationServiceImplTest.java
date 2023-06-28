package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "3214";
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService,
                        roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        user = new User(ID, EMAIL, PASSWORD, Set.of(new Role(USER_ROLE)));
    }

    @Test
    void register_ok() {
        when(roleService.getRoleByName(USER_ROLE.name())).thenReturn(new Role(USER_ROLE));
        when(userService.save(any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_ok() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = null;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            fail();
        }
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void login_invalidEmail_notOk() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, PASSWORD);
        }, "AuthenticationException is expected");
    }

    @Test
    void login_invalidPassword_notOk() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, INVALID_PASSWORD);
        }, "AuthenticationException is expected");
    }
}
