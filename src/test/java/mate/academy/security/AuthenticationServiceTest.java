package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private UserService userService;
    private RoleService roleService;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private User bob;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService
                = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        email = "bob@gmail.com";
        password = "Qwerty!234";
        bob = getUser(email, password, Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void register_valid_OK() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(bob);
        User actual = authenticationService.register(bob.getEmail(), bob.getPassword());
        assertNotNull(actual);
        assertEquals(bob.getEmail(), actual.getEmail());
        assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_valid_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(password, bob.getPassword())).thenReturn(true);
        User actual = authenticationService.login(email, password);
        assertNotNull(actual);
        assertEquals(email, actual.getEmail());
        assertEquals(password, actual.getPassword());
        assertEquals(actual, bob);
    }

    @Test
    void login_email_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(password, bob.getPassword())).thenReturn(true);
        try {
            authenticationService.login("com", password);
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_password_notOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(password, bob.getPassword())).thenReturn(false);
        try {
            authenticationService.login(email, "asdf");
        } catch (AuthenticationException e) {
            assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        fail("Expected to receive AuthenticationException");
    }

    @Test
    void shouldReturnAuthenticationException() {
        assertThrows(Exception.class, () -> {
            authenticationService.login("","");
        });
    }

    private User getUser(String email, String password, Set<Role> roles) {
        mate.academy.model.User user = new mate.academy.model.User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}
