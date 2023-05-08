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
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);

        String email = "alice@com.ua";
        user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        Role role = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(role));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register("alice@com.ua", "1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail("alice@com.ua")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("1234", "1234")).thenReturn(true);
        User actual = authenticationService.login("alice@com.ua", "1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_emailNotFound_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail("bob@com.ua")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches("1234", "1234")).thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("alice@com.ua", "1234"));
    }

    @Test
    void login_passwordNotFound_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail("alice@com.ua")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("1234", "1111")).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("alice@com.ua", "1234"));
    }
}
