package mate.academy.security;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    private User expected;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(1L);
        expected = new User();
        expected.setEmail("test@gmail.com");
        expected.setPassword("12345678");
        expected.setRoles(Set.of(userRole));
        expected.setId(1L);
        authenticationService
                = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void authentication_register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.save(any())).thenReturn(expected);
        User actual = authenticationService.register("test@gmail.com", "12345678");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void authentication_login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(expected));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login(expected.getEmail(), expected.getPassword());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void authentication_loginUncorrected_notOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService
                .login("UncorrectEmail", "12345678"));
    }
}
