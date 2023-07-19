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
    private static final String VALID_EMAIL = "bob";
    private static final String VALID_PASSWORD = "1234";
    private static final String NOT_VALID_EMAIL = "taras";
    private static final String EXPECTED = "User not found.";
    private static final String EXPECTED_MESSAGE = "Expected to receive UsernameNotFoundException";
    private User bob;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bob = new User();
        bob.setEmail(VALID_EMAIL);
        bob.setPassword(VALID_PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        try {
            userDetailsService.loadUserByUsername(NOT_VALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(EXPECTED, e.getMessage());
            return;
        }
        Assertions.fail(EXPECTED_MESSAGE);
    }
}
