package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String email = "bob@i.ua";
    private static User bob;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        bob = new User();
        bob.setEmail(email);
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(email))
                .thenReturn(Optional.of(bob));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals("1234", actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to recieve UsernameNotFoundException");
    }
}
