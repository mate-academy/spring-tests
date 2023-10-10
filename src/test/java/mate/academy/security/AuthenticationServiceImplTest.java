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
    private static final String EMAIL = "bob@com.ua";
    private static final String PASSWORD = "1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User savedUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(EMAIL);
        savedUser.setPassword(PASSWORD);
        savedUser.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(savedUser);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("bob@com.ua", actual.getEmail());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(true);
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedUser, actual);
    }

    @Test
    void login_wrongEmail_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("alice@gmail.com", PASSWORD));
    }

    @Test
    void login_wrongPassword_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));
        Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, "123"));
    }
}
