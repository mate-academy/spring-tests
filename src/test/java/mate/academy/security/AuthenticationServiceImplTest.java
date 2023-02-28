package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    private User emma;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = Mockito.mock(UserServiceImpl.class);
        roleService = Mockito.mock(RoleService.class);

        Role userRole = new Role(Role.RoleName.USER);
        emma = new User();
        emma.setRoles(Set.of(userRole));
        emma.setPassword(passwordEncoder.encode("emma1234"));
        emma.setEmail("emma@gmail.com");

        authenticationService = new AuthenticationServiceImpl(
                userService, roleService, passwordEncoder);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(userRole);
        Mockito.when(userService.findByEmail("emma@gmail.com"))
                .thenReturn(Optional.ofNullable(emma));
        Mockito.when(userService.save(Mockito.eq(emma))).thenReturn(emma);

    }

    @Test
    void register_correctUser_isOk() {
        emma.setPassword("emma1234");
        User actual = authenticationService.register("emma@gmail.com", "emma1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(emma, actual);
    }

    @Test
    void login_correctUser_isOk() throws AuthenticationException {
        User actual = authenticationService.login(emma.getEmail(), "emma1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(emma, actual);
    }

    @Test
    void login_notExistPassword_notOk() {
        try {
            authenticationService.login("emma@gmail.com", "bob1234");
        } catch (AuthenticationException ex) {
            Assertions.assertEquals(ex.getMessage(), "Incorrect username or password!!!");
            return;
        }
        Assertions.fail();
    }

    @Test
    void login_notExistEmail_notOk() {
        try {
            authenticationService.login("Bob@gmail.com", "emma1234");
        } catch (AuthenticationException ex) {
            Assertions.assertEquals(ex.getMessage(), "Incorrect username or password!!!");
            return;
        }
        Assertions.fail();
    }
}
