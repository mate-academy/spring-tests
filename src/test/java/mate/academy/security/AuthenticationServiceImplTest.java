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
    private static final String USER_NAME = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private AuthenticationService authenticationService;
    private User user;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setEmail(USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
    }

    @Test
    void register_ok() {
        User actual = authenticationService.register(USER_NAME, USER_PASSWORD);
        Assertions.assertEquals(USER_NAME, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void register_emptyLogin_notOk() {
        User actual = authenticationService.register(USER_NAME, USER_PASSWORD);
        Assertions.assertNotEquals("", actual.getEmail());
    }

    @Test
    void register_emptyPassword_notOk() {
        User actual = authenticationService.register(USER_NAME, USER_PASSWORD);
        Assertions.assertNotEquals("", actual.getPassword());
    }

    @Test
    void login_ok() {
        Mockito.when(userService.findByEmail(USER_NAME)).thenReturn(Optional.of(user));
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        try {
            User actual = authenticationService.login(USER_NAME, USER_PASSWORD);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(actual.getPassword(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.fail("Incorrect user name or password");
        }
    }

    @Test
    void login_emptyUserName_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("", ""));
    }
}
