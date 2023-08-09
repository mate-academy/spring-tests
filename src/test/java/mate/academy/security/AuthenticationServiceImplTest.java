package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private static final String EMAIL = "cat@gmail.com";
    private static final String PASSWORD = "12345678";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));

        User actual = authenticationService.register(EMAIL, PASSWORD);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches("12345678", PASSWORD)).thenReturn(true);

        User actual = authenticationService.login(PASSWORD, PASSWORD);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void login_IncorrectEmail_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches("12345678", PASSWORD)).thenReturn(true);

        assertThrows(AuthenticationException.class, () ->
                authenticationService.login("IncorrectEmail", PASSWORD));
    }
}
