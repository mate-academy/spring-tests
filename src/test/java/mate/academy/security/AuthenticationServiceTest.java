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

class AuthenticationServiceTest {
    private static String email;
    private static String password;
    private static User user;
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;
    private static RoleService roleService;
    private static AuthenticationService authenticationService;

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        email = "bob@i.ua";
        password = "123";
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        authenticationService =
                new AuthenticationServiceImpl(userService,roleService,passwordEncoder);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(user)).thenReturn(user);
        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email,actual.getEmail());
        Assertions.assertEquals("123",actual.getPassword());
        Assertions.assertEquals(Set.of(new Role(Role.RoleName.USER)),actual.getRoles());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(),
                Mockito.anyString())).thenReturn(true);
        User actual = authenticationService.login(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email,actual.getEmail());
        Assertions.assertEquals("123",actual.getPassword());
        Assertions.assertEquals(Set.of(new Role(Role.RoleName.USER)),actual.getRoles());
    }

    @Test
    void login_NotOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(),
                Mockito.anyString())).thenReturn(true);
        try {
            authenticationService.login("alice@i.ua", "34");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
