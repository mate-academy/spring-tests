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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

class AuthenticationServiceImplTest {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, roleService, passwordEncoder);
    }


    @Test
    void login_Ok() throws AuthenticationException {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        authenticationService.register("bob@i.ua", "1234");

        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(bob));

        User actual = authenticationService.login("bob@i.ua", "1234");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        Assertions.assertEquals("1234", actual.getPassword());
    }

    @Test
    void register_Ok() {

    }
  
}