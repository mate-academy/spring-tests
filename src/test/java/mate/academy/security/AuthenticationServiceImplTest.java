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

class AuthenticationServiceImplTest {
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
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
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void register_ValidData_Ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(USER_LOGIN, USER_PASSWORD);
        Assertions.assertEquals(USER_LOGIN, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_ValidData_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(),
                user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void login_InvalidLogin_NotOk() {
        Mockito.when(userService.findByEmail(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(user.getEmail(), user.getPassword()),
                "Expected to receive AuthenticationException.");
    }

    @Test
    void login_InvalidPassword_NotOk() {
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(),
                user.getPassword())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(user.getEmail(), user.getPassword()),
                "Expected to receive AuthenticationException.");
    }
}
