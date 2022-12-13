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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;

    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        user = createRawUser();
        user.setId(1L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register("user1@gmail.com", "12345678");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_validData_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail("user1@gmail.com")).thenReturn(Optional.of(user));
        User actual = authenticationService.login("user1@gmail.com", "12345678");
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_invalidEmail_notOk() {
        Mockito.when(userService.findByEmail("user1@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("user1@gmail.com", "12345678"));
    }

    @Test
    void login_invalidPassword_notOk() {
        Mockito.when(userService.findByEmail("user1@gmail.com")).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("user1@gmail.com", "87654321"));
    }

    private User createRawUser() {
        User user = new User();
        user.setEmail("user1@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
