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
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        String email = "bob@i.ua";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword("1234bob");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(bob));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("bob@i.ua");
        Assertions.assertNotNull(userDetails);
        Assertions.assertInstanceOf(
                org.springframework.security.core.userdetails.User.class, userDetails);
        Assertions.assertEquals("bob@i.ua", userDetails.getUsername(), "Username does not match");
        Assertions.assertEquals("1234bob", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());

        try {
            customUserDetailsService.loadUserByUsername("test@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected UsernameNotFoundException");
    }
}
