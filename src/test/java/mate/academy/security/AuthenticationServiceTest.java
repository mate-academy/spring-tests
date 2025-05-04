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

class AuthenticationServiceTest {
    public static final String EMAIL = "bob@gmail.com";
    public static final String PASSWORD = "12345";
    private AuthenticationService authenticationService;
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
                                                              roleService,
                                                              passwordEncoder);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);

    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(),
                user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());
        AuthenticationException authenticationException =
                Assertions.assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(user.getEmail(), user.getPassword()));
        Assertions.assertEquals("Incorrect username or password!!!",
                authenticationException.getMessage());
    }
}
