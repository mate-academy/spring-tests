package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.hibernate.validator.internal.util.Contracts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private UserService userService;
    private User admin;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
        admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword("super1234");
        admin.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(userService.save(any())).thenReturn(admin);
        User actual = authenticationService.register(admin.getEmail(), admin.getPassword());
        Contracts.assertNotNull(actual);
        assertEquals(admin, actual);
    }

    @Test
    void login_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(admin.getEmail()))
                .thenReturn(Optional.ofNullable(admin));
        Mockito.when(passwordEncoder.matches(admin.getPassword(),
                admin.getPassword())).thenReturn(true);
        User actual = authenticationService
                .login(admin.getEmail(), admin.getPassword());
        Contracts.assertNotNull(actual);
        assertEquals(admin, actual);
    }

    @Test
    void login_Not_Ok() throws AuthenticationException {
        Mockito.when(userService.findByEmail(admin.getEmail()))
                .thenReturn(Optional.ofNullable(admin));
        Mockito.when(passwordEncoder.matches(admin.getPassword(),
                admin.getPassword())).thenReturn(true);
        assertThrows(AuthenticationException.class,
                () -> authenticationService
                        .login(admin.getEmail(), "1234"));
    }
}
