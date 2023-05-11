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
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User bob;
    private String email = "bob@i.ua";
    private String password = "1234";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.save(any(User.class))).thenReturn(bob);
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(passwordEncoder.matches(bob.getPassword(),
                bob.getPassword())).thenReturn(true);
    }

    @Test
    void register_Ok() {
        User actual = authenticationService.register(email, password);
        Assertions.assertEquals(actual.getId(), bob.getId());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        User actual = authenticationService.login(email, password);
        Assertions.assertEquals(actual.getEmail(), bob.getEmail());
    }

    @Test
    void login_AuthenticationException_NotOk() {
        Mockito.when(userService.findByEmail("")).thenReturn(Optional.of(bob));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("wrongEmail", password));
    }
}
