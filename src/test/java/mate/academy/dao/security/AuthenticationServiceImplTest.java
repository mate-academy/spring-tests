package mate.academy.dao.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.security.AuthenticationServiceImpl;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_LOGIN = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(USER_LOGIN, USER_PASSWORD);
        Assertions.assertEquals(USER_LOGIN, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(USER_LOGIN)).thenReturn(Optional.of(user));
        User actual = null;
        try {
            actual = authenticationService.login(USER_LOGIN, USER_PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_notExistentValue_notOk() {
        String login = "login";
        String password = "password";
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(login, password));
    }
}
