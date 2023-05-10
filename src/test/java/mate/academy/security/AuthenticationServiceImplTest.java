package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static String expectedEmail;
    private static String expectedPassword;
    private static User user;
    private static Role defaultRole;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        expectedEmail = "vitaliy@i.ua";
        expectedPassword = "12345678";
        user = new User();
        user.setEmail(expectedEmail);
        user.setPassword(expectedPassword);
        defaultRole = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(defaultRole));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(defaultRole);
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(expectedEmail, expectedPassword);
        Assertions.assertEquals(expectedEmail, actual.getEmail());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(expectedEmail)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(expectedPassword, user.getPassword()))
                .thenReturn(true);
        User actual;
        try {
            actual = authenticationService.login(expectedEmail, expectedPassword);
        } catch (AuthenticationException e) {
            fail("Method login should return user");
            return;
        }
        Assertions.assertEquals(expectedEmail, actual.getEmail());
        Assertions.assertEquals(expectedPassword, actual.getPassword());
    }

    @Test
    void login_emptyUser() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login("something", "1234"));
    }

    @Test
    void login_badPasswordMatching() {
        Mockito.when(userService.findByEmail(expectedEmail)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login("vitaliy@i.ua", "12345"));
    }
}
