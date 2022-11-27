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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@e.mail";
    private static final String PASSWORD = "qwerty";
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(EMAIL, PASSWORD, Set.of(new Role(Role.RoleName.USER)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void register_ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        Assertions.assertEquals(user, authenticationService.register(EMAIL, PASSWORD));
    }

    @Test
    void login_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Assertions.assertEquals(user, authenticationService.login(EMAIL, PASSWORD));
    }

    @Test
    void login_incorrectData_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertEquals("Incorrect username or password!!!",
                Assertions.assertThrows(AuthenticationException.class,
                        () -> authenticationService.login(EMAIL, PASSWORD)).getMessage());
    }
}
