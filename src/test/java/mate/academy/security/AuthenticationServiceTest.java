package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.RoleServiceImpl;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;

class AuthenticationServiceTest {
        private AuthenticationService authenticationService;
        private UserService userService;
        private RoleService roleService;
        private PasswordEncoder passwordEncoder;
        private String email;
        private String incorrectEmail;
        private String password;
        private Set<Role> roles;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        roleService = Mockito.mock(RoleServiceImpl.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService,roleService,passwordEncoder);
        email = "bchupika@mate.academy";
        password = "12345678";
        roles = Set.of(new Role(Role.RoleName.USER));
        incorrectEmail = "asfdasfasff@gmail.com";
    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setId(1L);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(roles.stream().findFirst().get());
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual, "User must not be null for email: " + email + " password " + password);
    }

    @Test
    void login_Ok() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        user.setId(1L);
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
    void login_NotOK() {
        Mockito.when(userService.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthenticationException.class,
                () -> {authenticationService.login(incorrectEmail,password);},
                "AuthenticationException was expected with login: " + incorrectEmail
                        + " password: " + password);
    }
}