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
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User bob;

    @BeforeAll
     void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.save(any(User.class))).thenReturn(bob);
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(passwordEncoder.matches(bob.getPassword(),
                bob.getPassword())).thenReturn(true);
    }

    @Test
    void register_Ok() {
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(actual.getId(), bob.getId());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        User actual = authenticationService.login(EMAIL, PASSWORD);
        Assertions.assertEquals(actual.getEmail(), bob.getEmail());
    }

    @Test
    void login_AuthenticationException_NotOk() {
        Mockito.when(userService.findByEmail("")).thenReturn(Optional.of(bob));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("wrongEmail", PASSWORD));
    }
}
