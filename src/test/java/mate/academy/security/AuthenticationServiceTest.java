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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static AuthenticationService authenticationService;

    @BeforeAll
    public static void setUp() {
        // setting up and mocking dependencies
        UserService userService = Mockito.mock(UserService.class);
        RoleService roleService = Mockito.mock(RoleService.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);

        // setting up a single dummy user for testing in order to avoid code repetition
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(userRole));
        bob.setId(2L);

        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(userRole); // setting mocked roleService to return USER role
        Mockito.when(userService.save(ArgumentMatchers.any()))
                .thenReturn(bob); // for register tests
        Mockito.when(userService.findByEmail(ArgumentMatchers.any()))
                .thenReturn(Optional.of(bob)); // for login tests
        Mockito.when(userService.findByEmail("nosuchemail@gmail.com"))
                .thenReturn(Optional.empty()); // for not existing email test
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.eq("1234"),
                ArgumentMatchers.any())).thenReturn(true); // for valid password tests
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.eq("12345"),
                ArgumentMatchers.any())).thenReturn(false); // for invalid password tests
    }

    @Test
    public void register_ok() {
        User user = authenticationService.register("bob@i.ua", "1234");
        Assertions.assertEquals("bob@i.ua", user.getEmail());
        Assertions.assertEquals("1234", user.getPassword());
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Role.RoleName.USER, user.getRoles().stream()
                .findFirst().get().getRoleName());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void login_ok() throws AuthenticationException {
        User user = authenticationService.login("bob@i.ua", "1234");
        Assertions.assertEquals(user.getEmail(), "bob@i.ua");
        Assertions.assertEquals(user.getPassword(), "1234");
        Assertions.assertEquals(user.getRoles().size(), 1);
        Assertions.assertEquals(Role.RoleName.USER, user.getRoles().stream()
                .findFirst().get().getRoleName());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void login_incorrectPassword_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("bob@i.ua", "12345"));
    }

    @Test
    public void login_emptyUser_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("nosuchemail@gmail.com", "1234"));
    }
}
