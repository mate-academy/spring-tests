package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

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
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static final String TEST_PASSWORD = "12345";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
    }

    @Test
    void registerUser_UserRegistered_Ok() {
        User user = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(TEST_EMAIL, user.getEmail(),
                "The email or password of actual user doesn't match the expected data");
        Assertions.assertEquals(TEST_PASSWORD, user.getPassword(),
                "The email or password of actual user doesn't match the expected data");
    }

    @Test
    void registerUser_RegisterWithEmptyLogin_NotOk() {
        User actual = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotEquals("", actual.getEmail(),
                "The email shouldn't be empty");
    }

    @Test
    void registerUser_RegisterWithEmptyPassword_NotOk() {
        User actual = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotEquals("", actual.getPassword(),
                "The password shouldn't be empty");
    }

    @Test
    void login_AuthenticateWithValidEmailAndPassword_Ok() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder
                .matches(TEST_PASSWORD,testUser.getPassword())).thenReturn(true);
        try {
            authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail("AuthenticationException is not expected");
        }
    }

    @Test
    void login_AuthenticateWithIncorrectPassword_NotOk() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder
                .matches(TEST_PASSWORD,testUser.getPassword())).thenReturn(false);
        try {
            authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals(e.getMessage(),"Incorrect username or password!!!");
            return;
        }
        Assertions.fail("AuthenticationException for incorrect username or password expected");
    }

    @Test
    void login_AuthenticateWithEmptyEmail_NotOk() {
        Mockito.when(userService.findByEmail("")).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder
                .matches(TEST_PASSWORD,testUser.getPassword())).thenReturn(true);
        try {
            authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals(e.getMessage(),"Incorrect username or password!!!");
            return;
        }
        Assertions.fail("AuthenticationException for incorrect username or password expected");
    }
}
