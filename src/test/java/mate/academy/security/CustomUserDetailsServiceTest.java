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
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        bob = new User();
        bob.setPassword("bob54321");
        bob.setEmail("bob@i.ua");
        bob.setRoles(Set.of(userRole));
    }

    @Test
    void loadUserByUsername_correctUsername_isOk() {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(bob.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getUsername());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_notExistEmail_throwException() {
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException ex) {
            Assertions.assertEquals("User not found.", ex.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
