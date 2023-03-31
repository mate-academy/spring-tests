package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
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
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {
    private static final String TEST_USER_EMAIL = "testuser@i.ua";
    private static final String TEST_USER_PASSWORD = "87654321";
    private static User testUser;
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    
    @BeforeAll
    static void beforeAll() {
        testUser = new User();
        testUser.setPassword(TEST_USER_PASSWORD);
        testUser.setEmail(TEST_USER_EMAIL);
    }
    
    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
    }
    
    @Test
    void registerNewUser_ok() {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
        
        User actual = authenticationService.register(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(), testUser.getEmail());
        Assertions.assertEquals(actual.getPassword(), testUser.getPassword());
        Assertions.assertEquals(actual.getRoles(), testUser.getRoles());
    }
    
    @Test
    void register_WithDuplicateEmail_notOk() throws AuthenticationException {
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
    
        User registered = authenticationService.register(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        Mockito.when(userService.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(registered));
        
        Assertions.assertThrows(RuntimeException.class, () ->
                authenticationService.register(TEST_USER_EMAIL, TEST_USER_PASSWORD));
    }
    
    @Test
    void loginUser_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(TEST_USER_PASSWORD, TEST_USER_PASSWORD))
                .thenReturn(true);
        
        User actual = authenticationService.login(testUser.getEmail(), testUser.getPassword());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(TEST_USER_PASSWORD, actual.getPassword());
    }
    
    @Test
    void login_WithIncorrectEmail_notOk() {
        Mockito.when(userService.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(TEST_USER_EMAIL, TEST_USER_PASSWORD));
    }
    
    @Test
    void login_WithIncorrectPassword_notOk() {
        Mockito.when(userService.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches("", "")).thenReturn(false);
        assertThrows(AuthenticationException.class, () ->
                authenticationService.login(TEST_USER_EMAIL, TEST_USER_PASSWORD));
    }
}
