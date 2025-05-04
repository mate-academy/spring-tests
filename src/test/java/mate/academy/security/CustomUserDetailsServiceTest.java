package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomUserDetailsServiceTest {
    private UserService userService;
    private UserDetailsService userDetailsService;

    @Before
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername() {
        String email = "bob123@i.ua";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword("bob123");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(email, userDetails.getUsername());
    }
}
