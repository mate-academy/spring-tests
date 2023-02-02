package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String USER_EMAIL = "bob@gmai.ua";
    private static final String USER_PASSWORD = "dsd";
    private static final String OTHER_PASSWORD = "rhrhr";
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private User mockSavedUser;

    @BeforeEach
    void setUp() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        mockSavedUser = new User();
        mockSavedUser.setEmail(USER_EMAIL);
        mockSavedUser.setPassword(encoder.encode(USER_PASSWORD));

        userService = Mockito.mock(UserService.class);
        RoleService roleService = Mockito.mock(RoleService.class);

        authenticationService = new AuthenticationServiceImpl(userService, roleService, encoder);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(mockSavedUser);
        Mockito.when(roleService.getRoleByName(Mockito.anyString())).thenReturn(new Role());
    }

    @Test
    void register_oneValidUserExists_ok() {
        authenticationService.register(USER_EMAIL, USER_PASSWORD);
    }

    @Test
    void login_unregisteredUser_notOk() {
        authenticationService.register(USER_EMAIL, USER_PASSWORD);
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(USER_EMAIL, USER_PASSWORD),
                "Expected to throw error when logging in by some unexisting email");
    }

    @Test
    void login_wrongPasswordUser_notOk() {
        authenticationService.register(USER_EMAIL, USER_PASSWORD);
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockSavedUser));
        assertThrows(AuthenticationException.class, () ->
                        authenticationService.login(USER_EMAIL, OTHER_PASSWORD),
                "Expected to throw error when logging in with different password");
    }

    @Test
    void login_validUser_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockSavedUser));
        User actualUser = authenticationService.login(USER_EMAIL, USER_PASSWORD);
        assertEquals(mockSavedUser, actualUser,
                "Expected for valid user and his valid supplied password "
                        + "correctly login him and return the same user");
    }
}
