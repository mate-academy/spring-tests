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
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static RoleService roleService;
    private static PasswordEncoder passwordEncoder;
    private static final String EMAIL = "Test@gmail.com";
    private static final String PASSWORD = "1234";
    private static User test;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
        test = new User();
        test.setEmail(EMAIL);
        test.setPassword(PASSWORD);
        test.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(test);
    }

    @Test
    void register_Ok() {
        User user = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(EMAIL,user.getEmail());
        Assertions.assertEquals(PASSWORD,user.getPassword());
    }

    @Test
    void login_Authentication_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(test));
        Mockito.when(passwordEncoder.matches(PASSWORD,test.getPassword())).thenReturn(true);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail("Didn't expect to get an AuthenticationException");
        }
    }

    @Test
    void login_Authentication_Password_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(test));
        Mockito.when(passwordEncoder.matches(PASSWORD,test.getPassword())).thenReturn(false);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals(e.getMessage(),"Incorrect username or password!!!");
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void login_Authentication_User_NotOk() {
        Mockito.when(userService.findByEmail("")).thenReturn(Optional.of(test));
        Mockito.when(passwordEncoder.matches(PASSWORD,test.getPassword())).thenReturn(true);
        try {
            authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.assertEquals(e.getMessage(),"Incorrect username or password!!!");
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
