package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        String email = "boris@gmail.com";
        String password = "qwerty";
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        User boris = new User();
        boris.setEmail(email);
        boris.setPassword(password);
        boris.setRoles(Set.of(adminRole));

        Mockito.when(userService.findByEmail("boris@gmail.com")).thenReturn(Optional.of(boris));

        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals(password, actual.getPassword());
        Assertions.assertTrue(actual
                .getAuthorities()
                .toString()
                .matches("(.*)" + adminRole.getRoleName().name() + "(.*)"));
    }

    @Test
    void loadUserByUsername_WrongUsername_notOk() {
        try {
            userDetailsService.loadUserByUsername("wrong@mail.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }

    @Test
    void loadUserByUsername_NullUsername_notOk() {
        try {
            userDetailsService.loadUserByUsername(null);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }

    @Test
    void loadUserByUsername_EmptyUsername_notOk() {
        try {
            userDetailsService.loadUserByUsername("");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}