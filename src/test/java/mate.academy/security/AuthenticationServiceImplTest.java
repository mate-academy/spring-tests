package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "sobaka@mail.ru";
    private static final String  PASSWORD = "12345";
    private static final String NON_EXISTING_EMAIL = "neo@mail.com";
    private static final String  NON_EXISTING_PASSWORD = "777";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static User user;
    private static User non_existing_user;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        non_existing_user = new User();
        non_existing_user.setPassword(NON_EXISTING_PASSWORD);
        non_existing_user.setEmail(NON_EXISTING_EMAIL);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn((user));
        user.setRoles(Set.of(roleService.getRoleByName("USER")));
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        User actual;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
            Assertions.assertEquals(user, actual);
        } catch (AuthenticationException e) {
            Assertions.fail("Wrong email or password", e);
        }
    }

    @Test
    void login_NotOk() {
        Mockito.when(userService.findByEmail(NON_EXISTING_EMAIL)).thenReturn(Optional.of(non_existing_user));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(NON_EXISTING_EMAIL, NON_EXISTING_PASSWORD)
        );
    }
}
