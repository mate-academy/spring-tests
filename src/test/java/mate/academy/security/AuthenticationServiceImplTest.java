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
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);

        testUser = new User();
        testUser.setEmail("user@i.ua");
        testUser.setPassword("12345678");
        testUser.setRoles(Set.of((new Role(Role.RoleName.USER))));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
        User actual = authenticationService.register(testUser.getEmail(), testUser.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(testUser.getEmail(), testUser.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void login_invalidLogin_NotOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword()))
                .thenReturn(true);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(testUser.getEmail(), testUser.getPassword()),
                "Expected AuthenticationException ");
    }

    @Test
    void login_invalidPassword_NotOk() {
        Mockito.when(userService.findByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword()))
                .thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(testUser.getEmail(), testUser.getPassword()),
                "Expected AuthenticationException");
    }
}
