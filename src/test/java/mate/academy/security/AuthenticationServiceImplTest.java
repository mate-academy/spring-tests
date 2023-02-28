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
    private static final String EMAIL = "bob@domain.com";
    private static final String PASSWORD = "1234";
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
        user = createUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_NotExistentUser_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        AuthenticationException thrown = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
        Assertions.assertEquals("Incorrect username or password!!!", thrown.getMessage());
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
