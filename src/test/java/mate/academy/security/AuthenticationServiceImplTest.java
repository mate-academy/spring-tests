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
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String NOT_EXIST_USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private User testUser;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        testUser = Mockito.mock(User.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
    }

    @Test
    void register_Ok() {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);
        Mockito.when(userService.save(Mockito.any())).thenReturn(bob);
        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(USER_ROLE, actual.getRoles().stream().findAny().get());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        Mockito.when(passwordEncoder.matches(USER_PASSWORD,
                Optional.of(bob).get().getPassword())).thenReturn(true);
        User actual = authenticationService.login(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(USER_ROLE, actual.getRoles().stream().findAny().get());
    }

    @Test
    void login_LoginNotExist_NotOK() {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        try {
            authenticationService.login(NOT_EXIST_USER_EMAIL, USER_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException!");
    }
}
