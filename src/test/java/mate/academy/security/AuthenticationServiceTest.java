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

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    private final String EMAIL = "bobik@g.com";
    private final String NAME = "bobik";
    private final String PASSWORD = "1234567890";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    public void register_Ok() {
        User user = getUser();
        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User register = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(user.getEmail(), register.getEmail());
        Assertions.assertEquals(user.getPassword(), register.getPassword());
        Assertions.assertTrue(user.getRoles().equals(register.getRoles()));
    }

    @Test
    public void register_NullRole_NotOk() {
        Assertions.assertThrows(NullPointerException.class,
                () -> authenticationService.register(EMAIL, PASSWORD));
    }

    @Test
    public void login_Ok() throws AuthenticationException {
        User user = getUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User login = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(user.getEmail(), login.getEmail());
        Assertions.assertEquals(user.getPassword(), login.getPassword());
        Assertions.assertTrue(user.getRoles().equals(login.getRoles()));
    }

    @Test
    public void login_WrongEmail_NotOk() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(NAME, PASSWORD));
    }

    private User getUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}