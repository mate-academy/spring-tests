package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {
    private static String email;
    private static User user;
    private static UserDetailsService userDetailsService;
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        email = "bob@i.ua";
        user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_OK() {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(email,actual.getUsername());
        assertEquals("1234",actual.getPassword());
    }

    @Test
    void loadUserByUsername_NotOk() {
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail();
    }
}