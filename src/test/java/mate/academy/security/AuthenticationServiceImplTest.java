package mate.academy.security;

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
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceImplTest {
    private static final String LOGIN = "hello@i.am";
    private static final String ROLE_USER = "USER";
    private static final String PASSWORD = "1221";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User hello;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.spy(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        hello = new User();
        hello.setEmail(LOGIN);
        hello.setPassword(PASSWORD);
        hello.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(Mockito.any())).thenReturn(hello);
        Mockito.when(roleService.getRoleByName(ROLE_USER)).thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(), LOGIN);
        Assertions.assertEquals(actual.getPassword(), PASSWORD);
        Assertions.assertEquals(actual.getRoles(), hello.getRoles());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.ofNullable(hello));
        Mockito.when(passwordEncoder.matches(hello.getPassword(), PASSWORD)).thenReturn(true);
        User actual = authenticationService.login(LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
    }

    @Test
    void login_notOk() {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.ofNullable(hello));
        Mockito.when(passwordEncoder.matches(hello.getPassword(), "")).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(LOGIN, PASSWORD));
    }
}
