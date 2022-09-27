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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

public class AuthenticationServiceImplTest {
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail("modernboy349@gmail.com");
        user.setPassword("Hello123");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(roleService.getRoleByName(any()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(user);
        User actual = authenticationService.register(user.getEmail(), user.getPassword());
        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        User user = new User();
        user.setEmail("modernboy349@gmail.com");
        user.setPassword("Hello123");
        Mockito.when(userService.findByEmail("modernboy349@gmail.com"))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(user.getPassword(), "Hello123"))
                .thenReturn(true);
        User actual = authenticationService.login(user.getEmail(), user.getPassword());
        Assertions.assertEquals(user, actual);
    }
}
