package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private RoleService roleService;
    private User expectedUser;
    private UserService userService;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.spy(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        expectedUser = new User();
        expectedUser.setEmail("bchupika@mate.academy");
        expectedUser.setPassword("12345678");
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_validData_ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Matchers.any(User.class))).thenReturn(expectedUser);
        User actual =
                authenticationService.register(expectedUser.getEmail(), expectedUser.getPassword());
        assertNotNull(actual);
        assertEquals(expectedUser.getEmail(), actual.getEmail());
        assertEquals(expectedUser.getPassword(), actual.getPassword());
    }


    @Test
    void login_validData_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(
                        passwordEncoder.matches(expectedUser.getPassword(), expectedUser.getPassword()))
                .thenReturn(true);
        User actualUser =
                authenticationService.login(expectedUser.getEmail(), expectedUser.getPassword());
        assertNotNull(actualUser);
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    }

    @Test
    void login_invalidPassword_notOk() {
        Mockito.when(userService.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Mockito.when(passwordEncoder.matches(expectedUser.getPassword(), "wrongPassword"))
                .thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(expectedUser.getEmail(),
                        expectedUser.getPassword()));

    }
}
