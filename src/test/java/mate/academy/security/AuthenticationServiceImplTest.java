package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.RoleServiceImpl;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder encoder;
    private User expected;
    private static Long id = 1L;
    private static final String CORRECT_EMAIL = "DanaDiadius@gmail.com";
    private static final String INVALID_EMAIL = "@rambler.ru";
    private static final String PASSWORD = "1his1sMyPa55w0rd";
    private static Role role = new Role(Role.RoleName.USER);

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        roleService = Mockito.mock(RoleServiceImpl.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, encoder);

        expected = new User();
        expected.setId(id);
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(Set.of(role));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Mockito.any())).thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(expected);

        User actual = authenticationService.register(CORRECT_EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void login_Ok() {
        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(expected));
        Mockito.when(encoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        User actual;
        try {
            actual = authenticationService.login(CORRECT_EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail("Login should be successful with e-mail: " + CORRECT_EMAIL
                    + " and password: " + PASSWORD);
            return;
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }
}
