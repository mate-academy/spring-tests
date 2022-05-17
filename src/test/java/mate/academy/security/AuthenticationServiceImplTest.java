package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private UserUtilForTest userUtil;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userUtil = new UserUtilForTest();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService,
                passwordEncoder);
    }

    @Test
    void register_ok() {
        Role role = userUtil.getUserRole();
        User expected = userUtil.getUserBoris();
        expected.setRoles(Set.of(role));
        Mockito.when(roleService.getRoleByName(role.getRoleName().name())).thenReturn(role);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("1234");
        Mockito.when(userService.save(Mockito.any())).thenReturn(expected);

        User actual = authenticationService.register(expected.getEmail(), expected.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void login_ok () {
        User expected = userUtil.getUserNadja();
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(expected));
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        User actual = null;
        try {
            actual = authenticationService.login(expected.getEmail(), expected.getPassword());
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void login_NonExistentUser_notOk() {
        try {
            authenticationService.login("randomName","randomPassword");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Test failed, method 'login' not working correctly");
    }
}
