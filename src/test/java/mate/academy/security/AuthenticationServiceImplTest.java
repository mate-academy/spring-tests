package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

class AuthenticationServiceImplTest {
    private static final String NOT_EXISTENT_EMAIL = "Invalid";
    private static final String NOT_EXISTENT_PASSWORD = "Password";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
        user = new User();
        user.setEmail("user@email.com");
        user.setPassword("user12345");
    }

    @Test
    public void registerNewUser_Ok() {
        Mockito.when(roleService.getRoleByName(any(String.class)))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any(User.class))).thenReturn(user);
        User registeredUser = authenticationService.register(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(user, registeredUser,
                "Users must be the same. But they are not!");
    }

    @Test
    public void login_ExistentEmail_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        User loggedUser = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertNotNull(loggedUser);
        Assertions.assertEquals(user, loggedUser,
                "Users must be the same. But they are not!");
    }

    @Test
    void login_NotExistentEmail_NotOk() {
        try {
            authenticationService.login(NOT_EXISTENT_EMAIL, user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("AuthenticationException to be thrown, but nothing was thrown");
    }
}
