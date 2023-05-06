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

class AuthenticationServiceTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345678";
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
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
    }

    @Test
    void register_Ok() {
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertEquals(VALID_EMAIL, actual.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        user.setPassword(passwordEncoder.encode(VALID_PASSWORD));
        try {
            User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(actual.getPassword(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.fail("Incorrect email or password");
        }
    }

    @Test
    void register_emptyLogin_notOk() {
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotEquals("", actual.getEmail());
    }

    @Test
    void register_emptyPassword_notOk() {
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotEquals("", actual.getPassword());
    }

    @Test
    void login_emptyUserName_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("", ""));
    }
}
