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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static User user;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        role = new Role();
        role.setRoleName(ROLE);
    }

    @BeforeEach
    void setUp() {
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName(ROLE.name())).thenReturn(role);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), PASSWORD))
                .thenReturn(true);
        Assertions.assertEquals(user, authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_InvalidPassword_NotOk() {
        Mockito.when(userService.findByEmail("anotheremail@gmail.com"))
                .thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_InvalidLogin_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login("anotheremail@gmail.com",
                        PASSWORD));
    }
}
