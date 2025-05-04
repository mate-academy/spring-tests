package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static UserDetailsService userDetailsService;
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User bob = new User();
        bob.setEmail("bob123@gmail.com");
        bob.setPassword("12345678");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        UserDetails loadedUserDetails = userDetailsService.loadUserByUsername(bob.getEmail());

        Assertions.assertNotNull(loadedUserDetails);

        String expectedEmail = bob.getEmail();
        Assertions.assertEquals(expectedEmail, loadedUserDetails.getUsername());

        String expectedPassword = bob.getPassword();
        Assertions.assertEquals(expectedPassword, loadedUserDetails.getPassword());
    }

    @Test
    void loadUserByUsername_NotOk() {
        String unexistUserEmail = "alice1@gmail.com";
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(unexistUserEmail));
    }
}
