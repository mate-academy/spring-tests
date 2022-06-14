package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class AuthenticationServiceTest {
    private static final String USER_EMAIL = "user@mail.com";
    private static final String USER_PASSWORD = "user_password";
    private static final String ADMIN_EMAIL = "admin@mail.com";
    private static final String ADMIN_PASSWORD = "admin_password";
    private final UserService userService = Mockito.mock(UserService.class);
    private final RoleService roleService = Mockito.mock(RoleService.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final AuthenticationService authenticationService =
            new AuthenticationServiceImpl(userService, roleService, passwordEncoder);

    @BeforeEach
    void setUp() {
        Mockito.when(passwordEncoder.encode(anyString())).thenAnswer(i -> i.getArgument(0));
        Mockito.when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(i -> i.getArgument(0).equals(i.getArgument(1)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(anyString()))
                .thenAnswer(i -> new Role(Role.RoleName.valueOf(i.getArgument(0))));
        Mockito.when(userService.save(any())).thenAnswer(i -> i.getArgument(0));
        User actual = authenticationService.register(USER_EMAIL, USER_PASSWORD);
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getEmail());
        assertEquals(USER_PASSWORD, actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        List<Role> roleList = List.copyOf(actual.getRoles());
        assertEquals(Role.RoleName.USER, roleList.get(0).getRoleName());

        actual = authenticationService.register(ADMIN_EMAIL, ADMIN_PASSWORD);
        assertNotNull(actual);
        assertEquals(ADMIN_EMAIL, actual.getEmail());
        assertEquals(ADMIN_PASSWORD, actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        roleList = List.copyOf(actual.getRoles());
        assertEquals(Role.RoleName.USER, roleList.get(0).getRoleName());
    }

    @Test
    void register_roleServiceException_notOk() {
        Mockito.when(roleService.getRoleByName(anyString()))
                .thenThrow(DataProcessingException.class);
        Mockito.when(userService.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThrows(DataProcessingException.class,
                () -> authenticationService.register(USER_EMAIL, USER_PASSWORD));
    }

    @Test
    void register_userServiceException_notOk() {
        Mockito.when(roleService.getRoleByName(anyString()))
                .thenAnswer(i -> new Role(Role.RoleName.valueOf(i.getArgument(0))));
        Mockito.when(userService.save(any())).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class,
                () -> authenticationService.register(USER_EMAIL, USER_PASSWORD));
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(mockFindByEmail(USER_EMAIL, USER_PASSWORD)));
        User actual = authenticationService.login(USER_EMAIL, USER_PASSWORD);
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getEmail());
        assertEquals(USER_PASSWORD, actual.getPassword());

        Mockito.when(userService.findByEmail(ADMIN_EMAIL))
                .thenReturn(Optional.of(mockFindByEmail(ADMIN_EMAIL, ADMIN_PASSWORD)));
        actual = authenticationService.login(ADMIN_EMAIL, ADMIN_PASSWORD);
        assertNotNull(actual);
        assertEquals(ADMIN_EMAIL, actual.getEmail());
        assertEquals(ADMIN_PASSWORD, actual.getPassword());
    }

    @Test
    void login_userNotFound_notOk(){
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, USER_PASSWORD));
    }

    @Test
    void login_passNotMatch_notOk(){
        Mockito.when(userService.findByEmail(USER_EMAIL))
                .thenReturn(Optional.of(mockFindByEmail(USER_EMAIL, USER_PASSWORD)));
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(USER_EMAIL, ADMIN_PASSWORD));
    }

    private User mockFindByEmail(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
