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
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_userExist_Ok() {
        User user = new User();
        user.setEmail("bob@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail("bob@gmail.com")).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername("bob@gmail.com");
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getUsername());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_userExist_notOk() {
        try {
            userDetailsService.loadUserByUsername("notbob@gmail.com");
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("Expected - UsernameNotFoundException: User not found.");
    }
}
