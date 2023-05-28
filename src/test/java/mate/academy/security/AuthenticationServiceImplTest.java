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
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private UserService userService;
    private RoleService roleService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, PASSWORD_ENCODER);
    }

    @Test
    void register_validCredentials_ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        User user = authenticationService.register("user@gmail.com", "1234");

        Assertions.assertEquals("user@gmail.com", user.getEmail());
        Assertions.assertEquals("1234", user.getPassword());
        Assertions.assertEquals(Role.RoleName.USER,
                user.getRoles().stream().map(Role::getRoleName).findFirst().get());
    }

    @Test
    void login_validCredentials_ok() throws AuthenticationException {
        String login = "user@gmail.com";
        String password = "123456";
        User expected = new User();
        expected.setId(1L);
        expected.setEmail(login);
        expected.setPassword(PASSWORD_ENCODER.encode(password));
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(expected.clone()));
        User actual = authenticationService.login(login, password);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void login_invalidCredentials_notOk() {
        String validLogin = "user@gmail.com";
        String validPassword = "123456";
        User user = new User();
        user.setEmail(validLogin);
        user.setPassword(PASSWORD_ENCODER.encode(validPassword));
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(validLogin)).thenReturn(Optional.of(user));
        String invalidLogin = "admin@gmail.com";
        Mockito.when(userService.findByEmail(invalidLogin)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(validLogin, validPassword.substring(1)));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(invalidLogin, validPassword));
    }
}
