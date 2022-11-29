package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String EMAIL = "denys@mail.ru";
    private static final String PASSWORD = "12345678";
    private static final Role USER_ROLE = new Role();
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    static {
        USER_ROLE.setRoleName(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        User userWithId = new User();
        userWithId.setEmail(user.getEmail());
        userWithId.setPassword(user.getPassword());
        userWithId.setRoles(user.getRoles());
        userWithId.setId(1L);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);
        Mockito.when(userService.save(user)).thenReturn(userWithId);
        User registeredUser = authenticationService.register(EMAIL, PASSWORD);
        assertNotNull(registeredUser.getId());
        assertEquals(user.getEmail(), registeredUser.getEmail());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        assertEquals(Set.of(USER_ROLE), registeredUser.getRoles());
    }

    @Test
    void login_ok() throws AuthenticationException {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        authenticationService.login(EMAIL, PASSWORD);
    }

    @Test
    void login_notOk() throws AuthenticationException {
        String differentPassword = "differentPassword";
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, differentPassword));
    }
}
