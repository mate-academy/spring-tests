package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {

    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.save(any())).thenReturn(bob);
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role());
        User actual = authenticationService.register("bob@i.ua", "1234");
        Assert.assertNotNull(actual);
        Assert.assertEquals(bob.getEmail(), actual.getEmail());
        Assert.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_ok() throws AuthenticationException {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login("bob@i.ua", "1234");
        Assert.assertNotNull(actual);
        Assert.assertEquals(bob.getEmail(), actual.getEmail());
        Assert.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void login_UserNotFound() throws AuthenticationException {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        try {
            User actual = authenticationService.login("alice@i.ua", "1234");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
