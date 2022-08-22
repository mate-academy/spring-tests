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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("11111111");
        user.setId(1L);
        user.setRoles(Set.of(roleService.getRoleByName("USER")));
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        User actual = authenticationService.register("user@mail.com", "11111111");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_ok() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("12345678");
        user.setId(1L);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        Assertions.assertNotNull(user);
        User actual = null;
        try {
            actual = authenticationService.login(user.getEmail(), user.getPassword());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Incorrect username or password!!!", e);
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId().longValue());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void login_userIsEmpty_notOk() {
        Throwable exception = Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login("login", "1111");
        }, "AuthenticationException was expected");
        Assertions.assertEquals("Incorrect username or password!!!",
                exception.getLocalizedMessage());
    }

    @Test
    void login_passwordsDoNotMatch_notOk() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("12345678");
        user.setId(1L);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(false);
        Assertions.assertNotNull(user);
        Throwable exception = Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(user.getEmail(), user.getPassword());
        }, "AuthenticationException was expected");
        Assertions.assertEquals("Incorrect username or password!!!",
                exception.getLocalizedMessage());
    }

    @Test
    void login_loginDoesNotExist_notOk() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("12345678");
        user.setId(1L);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        Assertions.assertNotNull(user);
        Throwable exception = Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(user.getEmail(), user.getPassword());
        }, "AuthenticationException was expected");
        Assertions.assertEquals("Incorrect username or password!!!",
                exception.getLocalizedMessage());
    }
}
