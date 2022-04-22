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
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "12345";
    private AuthenticationService authenticationService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                                                            roleService,
                                                            passwordEncoder);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_OK() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));

        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void login_OK() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(USER_PASSWORD, user.getPassword())).thenReturn(true);

        try {
            User actual = authenticationService.login(USER_EMAIL, USER_PASSWORD);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(USER_EMAIL, actual.getEmail());
            Assertions.assertEquals(USER_PASSWORD, actual.getPassword());

        } catch (AuthenticationException e) {
            throw new RuntimeException("AuthenticationServiceImpl login test have failed.", e);
        }
    }

    @Test
    void login_NotOk() {
        Mockito.when(userService.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(new User()));

        try {
            authenticationService.login(USER_EMAIL, USER_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
