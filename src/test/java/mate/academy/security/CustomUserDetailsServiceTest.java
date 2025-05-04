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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadByUsername_Ok() {
        String email = "bob@i.ua";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals("12345", actual.getPassword());
    }

    @Test
    void loadByUsername_invalidUsername_notOk() {
        String email = "bob@i.ua";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }

        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
