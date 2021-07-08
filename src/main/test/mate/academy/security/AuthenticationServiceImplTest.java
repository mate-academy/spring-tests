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
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123456789";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
    }

    @Test
    void register_Ok() {

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loginEmptyUser() {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (Exception e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
        }
    }

    @Test
    void loginNotValidPassword() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (Exception e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
        }
    }
}
