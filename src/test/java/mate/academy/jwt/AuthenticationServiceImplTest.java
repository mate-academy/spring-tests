package mate.academy.jwt;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_MAIL = "test@mail.com";
    private static final String USER_PASSWORD = "testPassword";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService,
                roleService,
                passwordEncoder);
        user = new User();
        user.setEmail(USER_MAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void registerUser_Ok() {
        Mockito.when(roleService.getRoleByName(Mockito.any()))
                        .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actualUser = authenticationService.register(USER_MAIL, USER_PASSWORD);
        Assertions.assertEquals(actualUser.getEmail(), user.getEmail());
        Assertions.assertEquals(actualUser.getPassword(), user.getPassword());
        Assertions.assertEquals(actualUser.getRoles(), user.getRoles());
    }

    @Test
    void findByLogin_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), USER_PASSWORD)).thenReturn(true);
        User actualUser = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertEquals(actualUser.getEmail(), user.getEmail());
        Assertions.assertEquals(actualUser.getPassword(), user.getPassword());
    }

    @Test
    void findByLogin_invalidLogin_notOk() {
        AuthenticationException authenticationException =
                Assertions.assertThrows(AuthenticationException.class,
                        () -> authenticationService.login("Invalid login",
                                "invalid password"));
        Assertions.assertEquals("Incorrect username or password!!!",
                authenticationException.getMessage());
    }
}
