package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void registerNewUser_Ok() {
        User exampleUser = new User();
        exampleUser.setEmail(EMAIL);
        exampleUser.setPassword(PASSWORD);
        exampleUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        exampleUser.setId(1L);
        Mockito.when(userService.save(any())).thenReturn(exampleUser);
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        User actualUser = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(exampleUser, actualUser);
    }

    @Test
    void loginUser_Ok() throws AuthenticationException {
        User exampleUser = new User();
        exampleUser.setEmail(EMAIL);
        exampleUser.setPassword(PASSWORD);
        exampleUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(exampleUser));
        Mockito.when(passwordEncoder
                .matches(eq(PASSWORD), eq(exampleUser.getPassword()))).thenReturn(true);
        User actualUser;
        actualUser = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(exampleUser, actualUser);
    }

    @Test
    void login_incorrectPassword_NotOk() {
        User exampleUser = new User();
        exampleUser.setEmail(EMAIL);
        exampleUser.setPassword(PASSWORD);
        exampleUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.of(exampleUser));
        Mockito.when(passwordEncoder
                .matches(eq(PASSWORD), eq(exampleUser.getPassword()))).thenReturn(true);
        User actualUser;
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(EMAIL, "1235"));
    }
}
