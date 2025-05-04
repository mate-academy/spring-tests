package mate.academy.security;

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

import java.util.Optional;
import java.util.Set;

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
        String email = "bob@i.ua";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actualUserDetails);
        Assertions.assertEquals(email, actualUserDetails.getUsername());
        Assertions.assertEquals(password, actualUserDetails.getPassword());
    }

    @Test
    void loadUserByUsername_exception() {
        String email = "bob@i.ua";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        String expected = "User not found.";
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            String actual = e.getMessage();
            Assertions.assertEquals(expected, actual);
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}