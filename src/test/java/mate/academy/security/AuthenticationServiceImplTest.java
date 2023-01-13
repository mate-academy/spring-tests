package mate.academy.security;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {

    private static final String EMAIL = "ester.ua";
    private static final String PASSWORD = "987654321";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_Ok() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User actual = null;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_wrongInputData_notOk() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, "password"));
    }
}
