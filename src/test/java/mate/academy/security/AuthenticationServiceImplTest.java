package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User bob;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        bob = new User(EMAIL, PASSWORD);
    }

    @Test
    void register_validInput_Ok() {
        Role userRole = new Role(Role.RoleName.USER);
        when(roleService.getRoleByName("USER")).thenReturn(userRole);
        when(userService.save(any())).thenReturn(bob);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        bob.setRoles(Set.of(userRole));
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_validInput_Ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        when(passwordEncoder.matches(PASSWORD, bob.getPassword())).thenReturn(true);
        User actual = null;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            fail("Exception was thrown with message: " + e.getMessage());
        }
        assertNotNull(actual);
        assertEquals(bob, actual);
    }

    @Test
    void login_noSuchUserInDB_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_wrongPassword_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        when(passwordEncoder.matches(PASSWORD, bob.getPassword())).thenReturn(false);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }
}
