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
    private String userEmail;
    private String userPassword;
    private AuthenticationService authenticationService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                                                            roleService,
                                                            passwordEncoder);
        userEmail = "user@gmail.com";
        userPassword = "12345";
        user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(userService.save(any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(userEmail, userPassword);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userEmail, actual.getEmail());
        Assertions.assertEquals(userPassword, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(userPassword, user.getPassword())).thenReturn(true);
        User actual = authenticationService.login(userEmail, userPassword);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userEmail, actual.getEmail());
        Assertions.assertEquals(userPassword, actual.getPassword());
    }

    @Test
    void login_nonExistentUser_Ok() {
        Mockito.when(userService.findByEmail(userEmail))
                .thenReturn(Optional.of(new User()));
        Assertions.assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(userEmail, userPassword),
                "Expected AuthenticationException for nonExistent User");
    }
}
