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
    private static final String EMAIL = "testmail@i.ua";
    private static final String PASSWORD = "12345";
    private static final long ID = 1L;
    private static final String ENCODER_PASS = "111111111";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService auth;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        auth = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User(ID, EMAIL, PASSWORD, ROLES);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);

        User actual = auth.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void login_Ok() {
        user.setPassword(ENCODER_PASS);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, ENCODER_PASS)).thenReturn(true);
        User actual = null;
        try {
            actual = auth.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(ENCODER_PASS, actual.getPassword());
    }

    @Test
    void login_WrongPassword() {
        user.setPassword(ENCODER_PASS);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Assertions.assertThrows(AuthenticationException.class,
                () -> auth.login(EMAIL, PASSWORD));
    }

    @Test
    void login_UserNotFound() {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthenticationException.class,
                () -> auth.login(EMAIL, PASSWORD));
    }

    @Test
    void register_RoleNotFound() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class,
                () -> auth.register(EMAIL, PASSWORD), "Role not found");
    }
}
