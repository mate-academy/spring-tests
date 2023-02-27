package mate.academy.security;

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
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "qwerty";
    private static final String USER_INCORRECT_PASSWORD = "asdfgh";
    private static final String NOT_EXISTENT_USER_EMAIL = "alice@i.ua";
    private static final String NEW_USER_EMAIL = "mate@i.ua";
    private static final String NEW_USER_PASSWORD = "ytrewq";
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;
    private static User existentUser;
    private static Role userRole;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, roleService,
                passwordEncoder);
        userRole = new Role(Role.RoleName.USER);
        existentUser = new User();
        existentUser.setId(1L);
        existentUser.setEmail(USER_EMAIL);
        existentUser.setPassword(passwordEncoder.encode(USER_PASSWORD));
        existentUser.setRoles(Set.of(userRole));
    }

    @Test
    public void register_Ok() {
        Mockito.when(userService.save(Mockito.any(User.class)))
                .then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        User registeredUser = authenticationService.register(NEW_USER_EMAIL, NEW_USER_PASSWORD);
        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(NEW_USER_EMAIL, registeredUser.getEmail());
        Assertions.assertEquals(NEW_USER_PASSWORD, registeredUser.getPassword());
        Assertions.assertTrue(registeredUser.getRoles().size() == 1
                && registeredUser.getRoles().contains(userRole));
    }

    @Test
    public void login_correctCredentials_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(existentUser));
        User actual = authenticationService.login(USER_EMAIL, USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(USER_PASSWORD, actual.getPassword()));
    }

    @Test
    public void login_userDoesntExist_authenticationException_NotOk() {
        Mockito.when(userService.findByEmail(NOT_EXISTENT_USER_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(NOT_EXISTENT_USER_EMAIL, USER_PASSWORD));
    }

    @Test
    public void login_incorrectPassword_authenticationException_NotOk() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(existentUser));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, USER_INCORRECT_PASSWORD));
    }
}
