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

    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private String login;
    private String password;
    private Role userRole;

    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);

        login = "example@gmail.com";
        password = "1234";
        userRole = new Role(Role.RoleName.USER);

        user = new User();
        user.setEmail(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(userRole));
    }

    @Test
    void register_ok() {
        user.setPassword("1234");

        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.save(Mockito.eq(user))).thenReturn(user);

        User actual = authenticationService.register(login, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoles(), Set.of(userRole));
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(user));

        User actual = authenticationService.login(login, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_PasswordNotMatches_notOk() {
        String nonCorrectPassword = "12345";

        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(user));

        try {
            authenticationService.login(login, nonCorrectPassword);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Excepted to receive AuthenticationException");
    }

    @Test
    void login_emailNotExist_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.empty());

        try {
            authenticationService.login("example2@gmail.com", password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Excepted to receive AuthenticationException");
    }
}
