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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_LOGIN = "bob123@gmail.com";
    private static final String USER_PASSWORD = "bob123";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        bob = new User();
        bob.setEmail(USER_LOGIN);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void login_Ok() {
        bob.setPassword(passwordEncoder.encode(USER_PASSWORD));
        Mockito.when(userService.findByEmail(USER_LOGIN)).thenReturn(Optional.of(bob));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = null;
        try {
            actual = authenticationService.login(USER_LOGIN, USER_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(USER_LOGIN, actual.getEmail());
    }

    @Test
    void loginWrongPassword_NotOk() {
        bob.setPassword(passwordEncoder.encode(USER_PASSWORD));
        Mockito.when(userService.findByEmail(USER_LOGIN)).thenReturn(Optional.of(bob));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_LOGIN, "wrongPassword"));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(bob);
        User actual = authenticationService.register(USER_LOGIN, USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_LOGIN, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(bob.getRoles(), actual.getRoles());
    }
}
