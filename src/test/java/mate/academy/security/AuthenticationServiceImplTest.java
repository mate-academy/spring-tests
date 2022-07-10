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
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);

        bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(bob);

        User actual = authenticationService.register(bob.getEmail(), bob.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_validLogin_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(bob.getPassword(), bob.getPassword()))
                .thenReturn(true);

        User actual = authenticationService.login(bob.getEmail(), bob.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_invalidLogin_NotOk() {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(bob.getPassword(), bob.getPassword()))
                .thenReturn(true);

        try {
            authenticationService.login(bob.getEmail(), bob.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_invalidPassword_NotOk() {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(bob.getPassword(), bob.getPassword()))
                .thenReturn(false);

        try {
            authenticationService.login(bob.getEmail(), bob.getPassword());
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
