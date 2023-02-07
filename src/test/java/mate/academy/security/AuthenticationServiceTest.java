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

class AuthenticationServiceTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService =
                new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(roleService.getRoleByName("USER"))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User actual = authenticationService.register("bob@i.ua", "12345");
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        Assertions.assertEquals("12345", actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_Ok() throws AuthenticationException {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        user.setPassword(passwordEncoder.encode("12345"));
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(user));
        User actual = authenticationService.login("bob@i.ua", "12345");
        Assertions.assertEquals(user,actual);
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void login_NotOk() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(user));
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("alice@i.ua", "123456"));
    }
}
