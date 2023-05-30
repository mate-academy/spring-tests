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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static final User expectedUser = new User();
    private static final String EMAIL_BOB = "bob@gmail.com";
    private static final String PASSWORD_BOB = "12345";

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);

        expectedUser.setEmail(EMAIL_BOB);
        expectedUser.setPassword(PASSWORD_BOB);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(roleService.getRoleByName(any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(expectedUser);
        Mockito.when(userService.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
    }

    @Test
    void register_Ok() {
        User registeredUser = authenticationService.register(EMAIL_BOB, PASSWORD_BOB);
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(expectedUser, registeredUser);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(passwordEncoder.matches(PASSWORD_BOB, expectedUser.getPassword()))
                .thenReturn(true);
        User loginedUser = authenticationService.login(EMAIL_BOB, PASSWORD_BOB);
        Assertions.assertNotNull(loginedUser);
        Assertions.assertEquals(loginedUser, expectedUser);
    }

    @Test
    void login_NotOk() {
        Mockito.when(passwordEncoder.matches(EMAIL_BOB, PASSWORD_BOB)).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL_BOB, PASSWORD_BOB));
    }
}
