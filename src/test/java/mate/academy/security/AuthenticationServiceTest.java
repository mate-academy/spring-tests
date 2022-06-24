package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.RoleServiceImpl;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;

class AuthenticationServiceTest {
        private static AuthenticationService authenticationService;
        private static UserService userService;
        private static RoleService roleService;
        private static PasswordEncoder passwordEncoder;
        private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserServiceImpl.class);
        roleService = Mockito.mock(RoleServiceImpl.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,roleService,passwordEncoder);
        String correctEmail = "bchupika@mate.academy";
        String password = "12345678";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Long identifier = 1L;
        user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        user.setId(identifier);
    }

    @Test
    void register_Ok() {
        String email = "bchupika@mate.academy";
        String password = "12345678";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(roles.stream().findFirst().get());
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual, "User must not be null for email: " + email + " password " + password);
    }

    @Test
    void login_Ok() {
        String email = "bchupika@mate.academy";
        String password = "12345678";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        try {
            User actual = authenticationService.login(email, password);
            Assertions.assertNotNull(actual,
                    "User must not be null with login: " + email + " password: " + password);
        } catch (AuthenticationException e) {
            Assertions.assertDoesNotThrow(() -> {authenticationService.login(email,password);},
                    "Exception does not expected with login: " + email + " , password: " + password);
        }
    }

    @Test
    void login_nonExistentEmail_NotOK() {
        String incorrectEmail = "asfdasfasff@gmail.com";
        String password = "12345678";
        Mockito.when(userService.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> {authenticationService.login(incorrectEmail,password);},
                "AuthenticationException was expected with login: " + incorrectEmail
                        + " password: " + password);
    }
}
