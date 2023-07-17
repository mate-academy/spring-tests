package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "ex@g.com";
    private static final String VALID_PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        assertNotNull(actual);
        assertEquals(VALID_EMAIL, actual.getUsername());
        assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        try {
            userDetailsService.loadUserByUsername("wrong-email@g.com");
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("Expected to receive UsernameNotFoundException");
    }
}
