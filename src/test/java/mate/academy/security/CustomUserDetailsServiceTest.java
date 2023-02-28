package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static CustomUserDetailsService userDetailsService;
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadByUsername_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(bob.getEmail()))
                .thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(bob.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getUsername());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void loadByUsername_UsernameNotFound() {
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
            Assertions.fail("Expected to receive UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
        }
    }
}
