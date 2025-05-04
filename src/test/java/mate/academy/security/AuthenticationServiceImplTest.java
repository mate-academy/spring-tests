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

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String VALID_PASSWORD = "1234567";
    private static final String WRONG_PASSWORD = "1234567";
    private static final String USER_ROLE = "USER";
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User userBob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        userBob = new User();
        userBob.setPassword(VALID_PASSWORD);
        userBob.setEmail(EMAIL);
        userBob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Mockito.when(roleService.getRoleByName(USER_ROLE)).thenReturn(roleUser);
        Mockito.when(userService.save(any())).thenReturn(userBob);
        User actual = authenticationService.register(EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, userBob.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, userBob.getPassword());
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, userBob.getPassword()))
                .thenReturn(true);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(userBob));
        User actual = authenticationService.login(EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, userBob);
        Assertions.assertEquals(EMAIL, userBob.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, userBob.getPassword());
    }

    @Test
    void login_WrongPassword_notOk() {
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, WRONG_PASSWORD)).thenReturn(false);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(userBob));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(VALID_PASSWORD, WRONG_PASSWORD));
    }
}
