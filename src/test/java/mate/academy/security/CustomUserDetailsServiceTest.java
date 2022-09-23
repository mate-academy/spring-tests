package mate.academy.security;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        String email = "email@email.io";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        UserDetails actualUserDetails = customUserDetailsService.loadUserByUsername(email);
        assertNotNull(actualUserDetails);
        assertEquals(email, actualUserDetails.getUsername());
        assertEquals(password, actualUserDetails.getPassword());
        assertEquals(1, actualUserDetails.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        String email = "email@email.io";
        String password = "1234";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        try {
            customUserDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
