package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String INCORRECT_USER_EMAIL = "incorrect.email";
    private static final String USER_PASSWORD = "password";
    private static final Role.RoleName USER = Role.RoleName.USER;
    private static final String ROLE_NAME = "USER";
    private AuthenticationService underTest;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        underTest = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        role = new Role(USER);
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void registerSuccess() {
        when(userService.save(any(User.class))).thenReturn(user);
        when(roleService.getRoleByName(any(String.class))).thenReturn(role);
        User actual = underTest.register(USER_EMAIL, USER_PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void registerException() {
        when(roleService.getRoleByName(ROLE_NAME)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> underTest
                .register(USER_EMAIL, USER_PASSWORD), "Role not found");
    }

    @Test
    void loginSuccess() throws AuthenticationException {
        when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(USER_PASSWORD, USER_PASSWORD)).thenReturn(true);
        User actual = underTest.login(USER_EMAIL, USER_PASSWORD);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void loginException() {
        when(userService.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(USER_PASSWORD, USER_PASSWORD)).thenReturn(true);
        assertThrows(AuthenticationException.class, () -> underTest
                .login(INCORRECT_USER_EMAIL, USER_PASSWORD));
    }
}
