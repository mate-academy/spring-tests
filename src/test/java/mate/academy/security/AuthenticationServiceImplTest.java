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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService
                = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User den = createUserDen();
        Mockito.when(userService.save(any(User.class))).thenReturn(den);
        Mockito.when(roleService.getRoleByName(eq(Role.RoleName.USER.name())))
                .thenReturn(new Role(Role.RoleName.USER));
        User actualUser = authenticationService.register(den.getEmail(), den.getPassword());
        Assertions.assertEquals(den.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(den.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(den.getRoles().size(), actualUser.getRoles().size());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        User den = createUserDen();
        Mockito.when(userService.findByEmail(den.getEmail())).thenReturn(Optional.of(den));
        Mockito.when(passwordEncoder.matches(den.getPassword(),
                den.getPassword())).thenReturn(true);
        User actualUser = authenticationService.login(den.getEmail(), den.getPassword());
        Assertions.assertEquals(den.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(den.getPassword(), actualUser.getPassword());
    }

    @Test
    void login_NotOk_AuthenticationException() throws AuthenticationException {
        User den = createUserDen();
        Mockito.when(userService.findByEmail(den.getEmail())).thenReturn(Optional.of(den));
        AuthenticationException exception = Assertions
                .assertThrows(AuthenticationException.class,
                        () -> {
                            User actualUser = authenticationService
                                    .login(den.getEmail(), den.getPassword());
                        }, "AuthenticationException was expected");
        Assertions.assertEquals("Incorrect username or password!!!", exception.getMessage());
    }

    public User createUserDen() {
        String password = "12345678";
        String email = "den@gmail.com";
        User den = new User();
        den.setPassword(password);
        den.setEmail(email);
        den.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return den;
    }
}
