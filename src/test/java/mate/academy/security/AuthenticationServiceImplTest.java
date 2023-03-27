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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static User defaultUser;
    private static Role userRole;
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void initUser() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(1L);
        defaultUser = new User();
        defaultUser.setEmail("default@gmail.com");
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }

    @Test
    void register_correct_ok() {
        defaultUser.setId(1L);
        defaultUser.setPassword("1234");
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.save(any()))
                .thenAnswer(i -> i.getArgument(0, User.class));
        User actual = authenticationService
                .register(defaultUser.getEmail(), defaultUser.getPassword());
        Assertions.assertEquals(Set.of(userRole), actual.getRoles());
    }

    @Test
    void login_correctInput_ok() throws AuthenticationException {
        defaultUser.setPassword(passwordEncoder.encode("12345"));
        Mockito.when(userService.findByEmail(any()))
                .thenReturn(Optional.ofNullable(defaultUser));
        User actual = authenticationService.login(defaultUser.getEmail(), "12345");
        Assertions.assertEquals(defaultUser.getEmail(), actual.getEmail(),
                "The user must log in, incorrect email");
        Assertions.assertEquals(defaultUser.getId(), actual.getId(),
                "The user must log in, incorrect id");
    }

    @Test
    void login_incorrectPasswordAndLogin_notOk() throws AuthenticationException {
        defaultUser.setPassword(passwordEncoder.encode("12345"));
        Mockito.when(userService.findByEmail(defaultUser.getEmail()))
                .thenReturn(Optional.ofNullable(defaultUser));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(defaultUser.getEmail(), "123456"),
                "The must be an error, because the passwords don't match");
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("testuser", "12345"),
                "The must be an error, because the email don't match");
    }
}
