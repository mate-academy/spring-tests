package mate.academy.security;

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
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static User userBob;
    private static User userAlice;
    private static String bobPassword;
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);

        initializeUsers();
    }

    private static void initializeUsers() {
        userBob = new User();
        userBob.setEmail("bob@i.ua");
        bobPassword = "1234";
        userBob.setPassword(passwordEncoder.encode(bobPassword));
        userBob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userAlice = new User();
        userAlice.setEmail("alice@i.ua");
        userAlice.setPassword(passwordEncoder.encode("4321"));
        userAlice.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn((userBob));

        User actual = authenticationService.register(userBob.getEmail(), bobPassword);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userBob.getEmail(), actual.getEmail());
        Assertions.assertEquals(userBob.getPassword(), actual.getPassword());
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(userBob.getEmail())).thenReturn(Optional.of(userBob));

        User actual;
        try {
            actual = authenticationService.login(userBob.getEmail(), bobPassword);
        } catch (AuthenticationException e) {
            Assertions.fail("Unexpected AuthenticationException");
            return;
        }

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userBob.getEmail(), actual.getEmail());
        Assertions.assertEquals(userBob.getPassword(), actual.getPassword());
    }

    @Test
    void login_IncorrectCredentials() {
        Mockito.when(userService.findByEmail(userAlice.getEmail())).thenReturn(Optional.of(userBob));

        try {
            authenticationService.login(userAlice.getEmail(), userAlice.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}