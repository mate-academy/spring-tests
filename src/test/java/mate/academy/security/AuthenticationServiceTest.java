package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "test@mail.com";
    private static final String PASSWORD = "1234";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private User user;
    private Role role;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        role = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(role));
    }

    @Test
    public void register_ok() {
        when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(role);
        when(userService.save(any())).thenReturn(user);
        User register = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(register);
        Assertions.assertEquals(ID, register.getId());
        Assertions.assertEquals(EMAIL, register.getEmail());
        Assertions.assertEquals(PASSWORD, register.getPassword());
    }

    @Test
    public void login_ok() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        User login = null;
        try {
            login = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
    }

    @Test
    public void login_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, PASSWORD));
    }
}
