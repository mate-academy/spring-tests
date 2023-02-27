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
    private static final String EMAIL = "bob@g.com";
    private static final String PASSWORD = "1234567890";
    private static final String USER_ROLE = "USER";
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(USER_ROLE)).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User register = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(user.getEmail(), register.getEmail());
        Assertions.assertEquals(user.getPassword(), register.getPassword());
        Assertions.assertEquals(user.getRoles(), register.getRoles());

    }

    @Test
    public void register_nullRole_notOk() {
        Assertions.assertThrows(NullPointerException.class,
                () -> authenticationService.register(EMAIL, PASSWORD));
    }

    @Test
    void login_validData_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), PASSWORD)).thenReturn(true);
        User actualUser = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(user, actualUser);

    }

    @Test
    void login_notValidData_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }
}
