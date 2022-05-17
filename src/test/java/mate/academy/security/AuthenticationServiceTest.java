package mate.academy.security;

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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_ok() {
        User expectedUser = new User();
        expectedUser.setEmail("bob@i.ua");
        expectedUser.setPassword("12345678");
        Role roleUser = new Role(Role.RoleName.USER);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(roleUser);
        Mockito.when(userService.save(any())).thenReturn(expectedUser);
        User actualUser = authenticationService.register(expectedUser.getEmail(),
                expectedUser.getPassword());
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void login_ok() {
        User expectedUser = new User();
        expectedUser.setEmail("bob@i.ua");
        expectedUser.setPassword("12345678");
        Mockito.when(userService.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(), expectedUser.getPassword()))
                .thenReturn(true);
        try {
            User actualUser = authenticationService.login(expectedUser.getEmail(),
                    expectedUser.getPassword());
            Assertions.assertNotNull(actualUser,
                    "User after login should not be null");
            Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail(),
                    "Email is not correct!");
            Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword(),
                    "Password is not correct or encoding done wrong!");
        } catch (AuthenticationException e) {
            Assertions.fail("AuthenticationException was thrown. Message: " + e.getMessage());
        }
    }

    @Test
    void login_exception() {
        User expectedUser = new User();
        expectedUser.setEmail("bob@i.ua");
        expectedUser.setPassword("12345678");
        Mockito.when(userService.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(), expectedUser.getPassword()))
                .thenReturn(true);
        try {
            User actualUser = authenticationService.login(expectedUser.getEmail(),
                    "incorrect");
            Assertions.assertNotNull(actualUser);
            Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
            Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}