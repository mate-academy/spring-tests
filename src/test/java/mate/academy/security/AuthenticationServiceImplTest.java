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

    @BeforeEach
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(
                userService,
                roleService,
                passwordEncoder
        );
    }

    @Test
    void register_validInput_ok() {
        String email = "some@email.com";
        String password = "1234";
        Role userRole = new Role(Role.RoleName.USER);
        Mockito.when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        User actualUser = authenticationService.register(email, password);
        Set<Role> expectedRoles = Set.of(userRole);
        Set<Role> actualRoles = actualUser.getRoles();
        Assertions.assertEquals(expectedRoles, actualRoles,
                "Method should create user with role %s\n"
                        .formatted(userRole.getRoleName().name()));
        Assertions.assertEquals(email, actualUser.getEmail(),
                "Method should create user with email: %s when this email passed as input\n"
                        .formatted(email));
    }

    @Test
    void login_validLoginAndPassword_ok() {
        String email = "some@email.com";
        String password = "1234";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(passwordEncoder.encode(password));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        try {
            User actualUser = authenticationService.login(email, password);
            Assertions.assertEquals(expectedUser, actualUser,
                    "Method should return user with email and password matching input params\n");
        } catch (AuthenticationException e) {
            Assertions.fail("Method should return user when valid login and email are passed\n");
        }
    }

    @Test
    void login_invalidPassword_notOk() {
        String email = "some@email.com";
        String password = "1234";
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(email, "wrong password"),
                "Method should throw %s when invalid password is passed\n"
                        .formatted(AuthenticationException.class));
    }

    @Test
    void login_invalidLogin_notOk() {
        String invalidEmail = "invalid@email.com";
        String password = "1234";
        Mockito.when(userService.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(invalidEmail, password),
                "Method should throw %s when invalid login is passed\n"
                        .formatted(AuthenticationException.class));
    }
}
