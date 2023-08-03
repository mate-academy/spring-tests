package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String ENCODED_PASSWORD = "111111111";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private static final long BOB_ID = 1L;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService auth;
    private User user;

    {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        auth = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        user = new User(BOB_ID, EMAIL, PASSWORD, ROLES);
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);

        User actual = auth.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void register_roleNotFound_notOk() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name())).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class,
                () -> auth.register(EMAIL, PASSWORD), "Role not found");
    }

    @Test
    void login_Ok() throws AuthenticationException {
        user.setPassword(ENCODED_PASSWORD);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        User actual = auth.login(EMAIL, PASSWORD);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(ENCODED_PASSWORD, actual.getPassword());
    }

    @Test
    void login_userNotFound_notOk() {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthenticationException.class,
                () -> auth.login(EMAIL, PASSWORD));
    }

    @Test
    void login_wrongPassword_notOk() {
        user.setPassword(ENCODED_PASSWORD);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Assertions.assertThrows(AuthenticationException.class,
                () -> auth.login(EMAIL, PASSWORD));
    }
}
