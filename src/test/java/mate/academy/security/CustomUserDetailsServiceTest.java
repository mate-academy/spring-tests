package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        User expected = new User();
        expected.setId(1L);
        expected.setEmail("harrington@.ua");
        expected.setPassword("12345678");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(expected.getEmail()))
                .thenReturn(Optional.of(expected));
        UserDetails userDetails = userDetailsService.loadUserByUsername(expected.getEmail());
        assertNotNull(userDetails);
        assertEquals(expected.getPassword(),userDetails.getPassword());
        assertEquals(expected.getEmail(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_userNameNotFound_notOk() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonsense"),
                "Should receive UserNotFoundException");
    }
}
