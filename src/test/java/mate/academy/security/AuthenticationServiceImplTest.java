package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private static UserService userService;
    @Mock
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;
    private static final String TEST_EMAIL = "some.name@test.test";
    private static final String TEST_PASSWORD = "123456";

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(
                userService,
                roleService,
                passwordEncoder
        );
    }

    @Test
    void register_validInput_ok() {
        Role userRole = new Role(USER);
        Mockito.when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        User actualUser = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Set<Role> expectedRoles = Set.of(userRole);
        Set<Role> actualRoles = actualUser.getRoles();
        Assertions.assertEquals(expectedRoles, actualRoles,
                "Method should create user with role " + userRole.getRoleName().name());
        Assertions.assertEquals(TEST_EMAIL, actualUser.getEmail(),
                "Method should create user with email: "
                        + TEST_EMAIL + " when this email passed as input");
    }

    @Test
    void login_validLoginAndPassword_ok() {
        User expectedUser = new User();
        expectedUser.setEmail(TEST_EMAIL);
        expectedUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(expectedUser));
        try {
            User actualUser = authenticationService.login(TEST_EMAIL, TEST_PASSWORD);
            Assertions.assertEquals(expectedUser, actualUser,
                    "Method should return user with email and password matching input params");
        } catch (AuthenticationException e) {
            Assertions.fail("Method should return user when valid login and email are passed");
        }
    }

    @Test
    void login_invalidPassword_notOk() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(TEST_EMAIL, "wrong password"),
                "Method should throw "
                        + AuthenticationException.class
                        + " when invalid password is passed");
    }

    @Test
    void login_newEmail_notOk() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(TEST_EMAIL, TEST_PASSWORD),
                "Method should throw "
                        + AuthenticationException.class
                        + " when invalid login is passed");
    }
}
