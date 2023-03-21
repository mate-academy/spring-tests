package mate.academy.security;

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

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

class AuthenticationServiceImplTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        String email = "bob@i.ua";
        String password = "1234";
        Role role = new Role(Role.RoleName.USER);
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(passwordEncoder.encode(password));
        bob.setRoles(Set.of(role));
        bob.setId(1L);
        userService.save(bob);


        Mockito.when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(roleService.getRoleByName("USER")).thenReturn(role);

        User actual = authenticationService.register(email, password);
        Assertions.assertNotNull(actual.getEmail());
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());


    }

    @Test
    void login_Ok() throws AuthenticationException {
        String email = "bob@i.ua";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(passwordEncoder.encode(password));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        User actual = authenticationService.login(email, password);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual);
    }
}