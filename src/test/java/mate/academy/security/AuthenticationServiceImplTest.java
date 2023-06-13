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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "qwerty";
    private static final String NOT_EXISTING_EMAIL = "alice@i.ua";
    private static final String NOT_EXISTING_PASSWORD = "123456";
    private static final String USER_ROLE_NAME = "USER";
    private static final String AUTHENTICATION_EXCEPTION_MESSAGE =
            "Incorrect username or password!!!";
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(USER_ROLE_NAME))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        //arrange
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        Long expectedId = 1L;

        //act
        User actual = authenticationService.login(EMAIL, PASSWORD);

        //assert
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedId, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void login_EmailIncorrect_NotOk() {
        //arrange
        Mockito.when(userService.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        //act & arrange
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(NOT_EXISTING_EMAIL, PASSWORD));
        Assertions.assertEquals(AUTHENTICATION_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void login_PasswordIncorrect_NotOk() {
        //arrange
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(NOT_EXISTING_PASSWORD, user.getPassword()))
                .thenReturn(false);

        //act & arrange
        AuthenticationException exception = Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, NOT_EXISTING_PASSWORD));
        Assertions.assertEquals(AUTHENTICATION_EXCEPTION_MESSAGE, exception.getMessage());
    }
}
