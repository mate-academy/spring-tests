package mate.academy.security;

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
    private AuthenticationService authenticationService;
    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        Long id = 1L;
        String email = "bob@i.ua";
        String password = "1234";

        User bob = new User();
        bob.setId(id);
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));

        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(bob);

        User actual = authenticationService.register(email, password);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
        Assertions.assertEquals(id, actual.getId());
    }

    @Test
    void login_Ok() {
        Long id = 1L;
        String email = "bob@i.ua";
        String password = "1234";

        User bob = new User();
        bob.setId(id);
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(true);

        User actual = null;
        try {
            actual = authenticationService.login(email, password);
        } catch (Exception e) {
            Assertions.fail("Correct logging information shouldn't throw exception", e);
        }

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
        Assertions.assertEquals(id, actual.getId());
    }

    @Test
    void login_incorrectPassword_NotOk() {
        String email = "bob@i.ua";
        String password = "1234";

        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(false);

        try {
            authenticationService.login(email, password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_incorrectLogin_NotOk() {
        String email = "bob@i.ua";
        String password = "1234";

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(true);

        try {
            authenticationService.login(email, password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
