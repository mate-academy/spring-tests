package mate.academy.security;


import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        Role roleUser = new Role(1L, Role.RoleName.USER);
        user = new User(1L, EMAIL, PASSWORD, Set.of(roleUser));
    }

    //метод save повертає null;
    @Test
    void register_validValue_ok() {
        Role roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setRoleName(Role.RoleName.USER);

        Mockito.when(roleService.getRoleByName("USER")).thenReturn(roleUser);
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register("bob@gmail.com", "1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_successLogin_ok() throws AuthenticationException {
        String expectedLogin = EMAIL;
        String expectedPassword = PASSWORD;

        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);

        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedLogin, actual.getEmail(),
                "Emails don't matches. Expected "
                        + expectedLogin + " but actual " + actual.getEmail());
        Assertions.assertEquals(expectedPassword, actual.getPassword(),
                "Passwords don't matches. Expected "
                        + expectedPassword + " but actual " + actual.getPassword());
    }

    @Test
    void login_userNotFound_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Method should throws AuthenticationException. User not found.");
    }

    @Test
    void login_passwordsDontMatch_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Method should throws AuthenticationException. Passwords don't match.");
    }
}