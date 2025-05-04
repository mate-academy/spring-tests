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
    private static final String EMAIL_LOGIN = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private User userFromDB;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setEmail(EMAIL_LOGIN);
        userFromDB.setPassword(PASSWORD);
        userFromDB.setRoles(Set.of(USER_ROLE));

    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail(EMAIL_LOGIN);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        Mockito.when(roleService.getRoleByName(USER_ROLE.getRoleName().name()))
                .thenReturn(USER_ROLE);
        Mockito.when(userService.save(user)).thenReturn(userFromDB);
        User actual = authenticationService.register(EMAIL_LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userFromDB.getId(), actual.getId());
        Assertions.assertEquals(userFromDB.getEmail(), actual.getEmail());
        Assertions.assertEquals(userFromDB.getPassword(), actual.getPassword());
        Assertions.assertTrue(actual.getRoles().contains(USER_ROLE));
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(EMAIL_LOGIN)).thenReturn(Optional.of(userFromDB));
        Mockito.when(passwordEncoder.matches(any(), any())).thenReturn(true);
        User actual = authenticationService.login(EMAIL_LOGIN, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userFromDB.getEmail(), actual.getEmail());
        Assertions.assertEquals(userFromDB.getPassword(), actual.getPassword());
    }
}
