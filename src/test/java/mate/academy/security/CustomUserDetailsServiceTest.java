package mate.academy.security;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123";

    private UserDetailsService userDetailsService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        String email = "bob@gmail.com";
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getUsername(),email);
        Assertions.assertEquals(actual.getPassword(),"123");
    }

    @Test
    public void loadUserByUsername_Not_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        String email = "bob@gmail.com";

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        try {
            UserDetails actual = userDetailsService.loadUserByUsername("alice@gmail.com");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.",e.getMessage());
            return;
        }
        fail("Expected to receive UsernameNotFoundException");
    }
}
