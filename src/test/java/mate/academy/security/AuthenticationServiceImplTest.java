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

public class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        user.setRoles(Set.of(new Role()));
        Mockito.when(userService.save(any())).thenReturn(user);
        User result = authenticationService
                .register("test@test.com", "test");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void login_ok() throws AuthenticationException {
        user.setRoles(Set.of(new Role()));
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User result = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void login_notOk() {
        user.setRoles(Set.of(new Role()));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(user.getEmail(), user.getPassword());
        });
    }
}
