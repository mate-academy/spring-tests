package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserService userService;
    private AuthenticationService authenticationService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
    }

    @Test
    void register_Ok() {
        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        assertEquals(USER_EMAIL, actual.getEmail());
        assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void register_emptyPassword_NotOk() {
        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        assertNotEquals("", actual.getPassword());
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        try {
            User actual = authenticationService.login(USER_EMAIL, USER_PASSWORD);
            Assertions.assertNotNull(actual);
            Assertions.assertEquals(actual.getPassword(), user.getPassword());
        } catch (AuthenticationException e) {
            Assertions.fail("Incorrect username or password!!!");
        }
    }

    @Test
    void login_emptyUser_NotOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("", ""));
    }

    @Test
    void login_passwordNotEncoder_NotOk() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, USER_PASSWORD));
    }
}
