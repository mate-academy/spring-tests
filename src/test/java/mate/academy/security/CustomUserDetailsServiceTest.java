package mate.academy.security;

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

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12344321";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User bob;

    @BeforeEach
    void setUp() {
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);

    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));

        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNameNotFoundException");
    }
}
