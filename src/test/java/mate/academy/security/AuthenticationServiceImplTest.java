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
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationServiceImpl authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        user = new User(EMAIL, PASSWORD, Set.of(ROLE));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(ROLE);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(),any())).thenReturn(true);
        Assertions.assertEquals(user,authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_WrongPassword_Exception_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(),any())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, PASSWORD);
        });
    }

    @Test
    void login_NotExistUser_Exception_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, PASSWORD);
        });
    }
}
