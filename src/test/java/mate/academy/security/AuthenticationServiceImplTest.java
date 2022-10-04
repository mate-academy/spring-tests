package mate.academy.security;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private User expected;
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        expected = new User();
        expected.setEmail(EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(Set.of(new Role(RoleName.USER)));
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService
                = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(RoleName.ADMIN));
        Mockito.when(userService.save(any())).thenReturn(expected);

        User actual = authenticationService.register(EMAIL, PASSWORD);

        assertEquals(expected, actual);
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expected));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);

        assertEquals(expected, actual);
    }

    @Test
    void login_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD));
    }
}
