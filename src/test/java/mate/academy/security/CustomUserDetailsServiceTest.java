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
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_existingUsername_ok() {
        String email = "my@i.ua";
        String password = "12345678";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(email);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void loadUserByUsername_notExistingUsername_notOk() {
        String email = "my@i.ua";
        String password = "12345678";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        try {
            userDetailsService.loadUserByUsername("nothing@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
