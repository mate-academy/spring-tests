package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "invalidEmail";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "5678";
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static AuthenticationService authenticationService;
    private static User testUser;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = 
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
    }
    
    @Test
    void register_ok() {      
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(testUser);
        User actual = authenticationService.register(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser, actual);
    }
    
    @Test
    void login_validEmailAndValidPass_ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(VALID_PASSWORD, testUser.getPassword()))
                .thenReturn(true);
        User actual = authenticationService.login(VALID_EMAIL, VALID_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getEmail());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }
    
    @Test
    void login_emptyUser_authenticationException() {       
        Mockito.when(userService.findByEmail(INVALID_EMAIL))
                .thenReturn(Optional.of(new User()));
        Assertions.assertThrows(AuthenticationException.class, () -> 
                authenticationService.login(INVALID_EMAIL, INVALID_PASSWORD));
    }
    
    @Test
    void login_validEmailAndInvalidPass_authenticationException() {       
        Mockito.when(userService.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.of(testUser));
        Mockito.when(!passwordEncoder.matches(INVALID_PASSWORD, testUser.getPassword()))
                .thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class, () -> 
                authenticationService.login(VALID_EMAIL, INVALID_PASSWORD));
    }
}
