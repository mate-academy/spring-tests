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
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        bob.setRoles(Set.of(userRole));
        User bobWithId = new User();
        bobWithId.setEmail("bob@i.ua");
        bobWithId.setPassword("1234");
        bobWithId.setRoles(Set.of(userRole));
        bobWithId.setId(2L);
        User bobWithDifferentCredentials = new User();
        bobWithDifferentCredentials.setEmail("bob@mail.ua");
        bobWithDifferentCredentials.setPassword("12345");
        bobWithDifferentCredentials.setRoles(Set.of(userRole));
        bobWithDifferentCredentials.setId(2L);
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.any(),
                ArgumentMatchers.eq("1234"))).thenReturn(true);
        Mockito.when(passwordEncoder.matches(ArgumentMatchers.any(),
                ArgumentMatchers.eq("12345"))).thenReturn(false);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.save(ArgumentMatchers.any())).thenReturn(bobWithId);
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(bobWithId));
        Mockito.when(userService.findByEmail("bob@mail.ua")).thenReturn(
                Optional.of(bobWithDifferentCredentials));
    }

    @Test
    public void register_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        User user = authenticationService.register("bob@i.ua", "1234");
        Assertions.assertEquals(user.getEmail(), "bob@i.ua");
        Assertions.assertEquals(user.getPassword(), "1234");
        Assertions.assertEquals(user.getRoles().size(), 1);
        for (Role role : user.getRoles()) {
            Assertions.assertEquals(role.getRoleName(), userRole.getRoleName());
        }
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void register_nullInput_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        User nullUser = new User();
        nullUser.setId(3L);
        nullUser.setRoles(Set.of(userRole));
        Mockito.when(userService.save(ArgumentMatchers.any())).thenReturn(nullUser);
        User user = authenticationService.register(null, null);
        Assertions.assertNull(user.getEmail());
        Assertions.assertNull(user.getPassword());
        Assertions.assertEquals(user.getRoles().size(), 1);
        for (Role role : user.getRoles()) {
            Assertions.assertEquals(role.getRoleName(), userRole.getRoleName());
        }
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void login_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        User user = null;
        try {
            user = authenticationService.login("bob@i.ua", "1234");
        } catch (AuthenticationException e) {
            Assertions.fail("Should not throw an AuthenticationException");
        }
        Assertions.assertEquals(user.getEmail(), "bob@i.ua");
        Assertions.assertEquals(user.getPassword(), "1234");
        Assertions.assertEquals(user.getRoles().size(), 1);

        for (Role role : user.getRoles()) {
            Assertions.assertEquals(role.getRoleName(), userRole.getRoleName());
        }
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void login_incorrectPassword_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("bob@mail.ua", "1234"));
    }

    @Test
    public void login_nullInput_notOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(null, null));
    }
}
