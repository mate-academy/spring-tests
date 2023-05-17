package mate.academy.security;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static final String EMAIl = "bob@gmail.com";
    private static final String PASSWORD = "123";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService,roleService,passwordEncoder);
    }

    @Test
    public void register_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail(EMAIl);
        bob.setPassword(PASSWORD);

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        bob.setRoles(Set.of(role));

        Mockito.when(userService.save(any())).thenReturn(bob);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);

        User actual = authenticationService.register("bob@gmail.com", "123");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob,actual);
    }

    @Test
    public void register_Duplicate_Email_Not_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail(EMAIl);
        bob.setPassword(PASSWORD);

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        bob.setRoles(Set.of(role));

        User bobClone = new User();
        bobClone.setId(1L);
        bobClone.setEmail(EMAIl);
        bobClone.setPassword(PASSWORD);
        bobClone.setRoles(Set.of(role));

        Mockito.when(userService.save(any())).thenReturn(bob);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);

        User actual = authenticationService.register("bob@gmail.com", "123");

        if (bobClone.getEmail().equals(actual.getEmail())) {
            Mockito.when(userService.save(any())).thenThrow(DataProcessingException.class);
        }

        Assertions.assertThrows(DataProcessingException.class,
                () -> authenticationService.register(bobClone.getEmail(),bobClone.getPassword()));

    }

    @Test
    public void login_Ok() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        User bob = new User();
        bob.setId(1L);
        bob.setEmail(EMAIl);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail("bob@gmail.com")).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches("123",bob.getPassword())).thenReturn(true);
        try {
            User actual = authenticationService.login("bob@gmail.com", "123");
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(bob,actual);
        } catch (AuthenticationException e) {
            fail("failed");
        }
    }

    @Test
    public void login_Incorrect_Password() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        User bob = new User();
        bob.setId(1L);
        bob.setEmail(EMAIl);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(role));

        String incorrectPassword = "555";

        Mockito.when(userService.findByEmail("bob@gmail.com"))
                .thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches("123", bob.getPassword()))
                .thenReturn(true);
        Mockito.when(passwordEncoder.matches(incorrectPassword, bob.getPassword()))
                .thenReturn(false);
        try {
            User actual = authenticationService.login("bob@gmail.com", "123");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",e.getMessage());
            return;
        }
        fail("Expected to receive AuthenticationException");
    }
}
