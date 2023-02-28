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
    private static final String EMAIL = "user@com.ua";
    private static final String PASSWORD = "password";
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(createUser());
        Mockito.when(userService.findByEmail(EMAIL))
                .thenReturn(Optional.of(createUser()));

        RoleService roleService = Mockito.mock(RoleService.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));

        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    public void register_ok() {
        User user = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(PASSWORD, user.getPassword());
        Assertions.assertEquals(1, user.getRoles().size());

    }

    @Test
    public void login_ok() {
        try {
            User user = authenticationService.login(EMAIL, PASSWORD);
            Assertions.assertNotNull(user);
        } catch (AuthenticationException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void login_wrongUser_notOk() {
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login("wrongLogin", PASSWORD));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
