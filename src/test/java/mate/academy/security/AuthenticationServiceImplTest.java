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
    private static final String EMAIL = "bob@i.com";
    private static final String PASSWORD = "12345678";
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
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_OK() throws AuthenticationException {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(),PASSWORD)).thenReturn(true);

        User actual = authenticationService.login(EMAIL, PASSWORD);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user,actual);
    }

    @Test
    void login_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(any(),any())).thenReturn(false);
        try {
            authenticationService.login(EMAIL,PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException ");
    }
}
