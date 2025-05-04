package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob";
    private static final String VALID_PASSWORD = "1234";
    private static final String ENCODED_PASSWORD = "encoded1234";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User bob;
    private Role role;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        role = new Role(Role.RoleName.USER);
        bob = new User();
        bob.setEmail(VALID_EMAIL);
        bob.setPassword(passwordEncoder.encode(VALID_PASSWORD));
        bob.setRoles(Set.of(role));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(role);
        Mockito.when(userService.save(any())).thenReturn(bob);
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, bob.getPassword())).thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        });
    }
}
