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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void registerUserByEmailAndPassword_Ok() {
        String email = "Max@i.ua";
        String password = "1234";
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        User max = new User();
        max.setEmail(email);
        max.setPassword(password);
        max.setRoles(Set.of(role));
        Mockito.when(userService.save(max)).thenReturn(max);
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);
        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
        Assertions.assertEquals(role, actual.getRoles().stream().findFirst().get());
    }

    @Test
    void loginUserByValidEmailAndPassword_Ok() throws AuthenticationException {
        String login = "Max@i.ua";
        String password = "1234";
        String securedPassword = passwordEncoder.encode(password);
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        User max = new User();
        max.setEmail(login);
        max.setPassword(securedPassword);
        max.setRoles(Set.of(role));
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(max));
        User actual = authenticationService.login(login, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual.getEmail());
        Assertions.assertEquals(securedPassword, actual.getPassword());
        Assertions.assertEquals(role, actual.getRoles().stream().findFirst().get());
    }

    @Test
    void loginUserByInvalidPassword_IncorrectUsernameOrPassword_notOk() {
        String login = "Max@i.ua";
        String password = "1234";
        String securedPassword = passwordEncoder.encode(password);
        String invalidPassword = "123";
        String invalidSecuredPassword = passwordEncoder.encode(invalidPassword);
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        User max = new User();
        max.setEmail(login);
        max.setPassword(securedPassword);
        max.setRoles(Set.of(role));
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(max));
        try {
            authenticationService.login(login, invalidSecuredPassword);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }

    @Test
    void loginUserByNotExistedEmail_IncorrectUsernameOrPassword_notOk() {
        String login = "Max@i.ua";
        String password = "1234";
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.empty());
        try {
            authenticationService.login(login, password);
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive AuthenticationException");
    }
}
