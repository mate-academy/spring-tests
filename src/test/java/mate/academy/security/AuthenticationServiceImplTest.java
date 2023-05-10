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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "11111111";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        roleService = Mockito.mock(RoleService.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_ok() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = null;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void login_notOk() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        Mockito.when(userService.findByEmail("ERROR")).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class, () -> {
            authenticationService.login(EMAIL, PASSWORD);
        }, "AuthenticationException expected");
    }
}
