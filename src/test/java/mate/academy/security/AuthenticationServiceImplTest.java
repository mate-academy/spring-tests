package mate.academy.security;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String EMPTY_PASSWORD = "";
    private User user;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void register_ok() {
        when(userService.save(any())).thenReturn(user);
        when(roleService.getRoleByName(any()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword()))
                .thenReturn(true);
        assertDoesNotThrow(() -> {
            Optional<User> actual = Optional.ofNullable(authenticationService.login(EMAIL,
                    PASSWORD));
            Assertions.assertFalse(actual.isEmpty());
            assertEquals(EMAIL, actual.get().getEmail());
            assertEquals(PASSWORD, actual.get().getPassword());
        });
    }

    @Test
    void login_badCredentials_notOk() {
        assertThrows(AuthenticationException.class, () -> {
            user.setPassword(EMPTY_PASSWORD);
            when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(user.getPassword(),
                    user.getPassword())).thenReturn(false);
            authenticationService.login(EMAIL, EMPTY_PASSWORD);
        });
    }
}
