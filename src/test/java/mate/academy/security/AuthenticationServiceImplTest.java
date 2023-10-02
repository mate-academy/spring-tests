package mate.academy.security;

import java.util.Optional;
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
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private String email;
    private String password;
    private User bobby;


    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,passwordEncoder);
        email = "bobby@mate.academy";
        password = "goodReviewForDOU";
        bobby = new User();
        bobby.setEmail(email);
        bobby.setPassword(password);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bobby));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        User actual = authenticationService.login(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(bobby);
        User actual = authenticationService.register(email,password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void login_Authentication() {
        Mockito.when(userService.save(bobby)).thenReturn(bobby);
        userService.save(bobby);
        try {
            authenticationService.login("incorrect@email.com", "freePassword");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}