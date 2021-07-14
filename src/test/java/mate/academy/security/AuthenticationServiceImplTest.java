package mate.academy.security;

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
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User expected;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        expected = new User();
        expected.setId(1L);
        expected.setEmail("lucy1234@gmail.com");
        expected.setPassword("12345tyr");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_ok() {
        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.USER);
        Mockito.when(userService.save(Mockito.any())).thenReturn(expected);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        User userFromDb = authenticationService.register("lucy1234@gmail.com", "12345tyr");
        Assertions.assertEquals(expected, userFromDb);
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail("lucy1234@gmail.com")).thenReturn(Optional.of(expected));
        Mockito.when(passwordEncoder.matches("12345tyr", "12345tyr")).thenReturn(true);
        Assertions.assertEquals(expected, authenticationService.login("lucy1234@gmail.com", "12345tyr"));
    }

    @Test
    void login_invalidPassword_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(expected));
        Assertions.assertThrows(Exception.class, () -> {
                    authenticationService.login("lucy1234@gmail.com", "1299999977777");
                }
        );
    }
}