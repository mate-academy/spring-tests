package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class CustomUserDetailsServiceTest {
    private static final String STATIC_USERNAME = "test@gmail.com";
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(STATIC_USERNAME);
        user.setId(1L);
        user.setPassword("pass");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        customUserDetailsService = new CustomUserDetailsService(userService);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(STATIC_USERNAME);
        assertNotNull(userDetails);
        assertInstanceOf(
                org.springframework.security.core.userdetails.User.class, userDetails);
        assertEquals(STATIC_USERNAME, userDetails.getUsername(), "Username does not match");
        assertEquals("pass", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        customUserDetailsService = new CustomUserDetailsService(userService);

        try {
            customUserDetailsService.loadUserByUsername(STATIC_USERNAME);
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("Expected UsernameNotFoundException");
    }
}