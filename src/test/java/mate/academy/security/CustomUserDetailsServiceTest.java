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
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual, "UserDetails can't be null");
        Assertions.assertEquals(EMAIL, actual.getUsername(),
                "Expected email: " + EMAIL
                        + " but was: " + actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword(),
                "Expected password: " + PASSWORD
                        + " but was: " + actual.getPassword());
    }

    @Test
    void loadUserByUsername_userIsNotInDb_ok() {
        try {
            userDetailsService.loadUserByUsername(INVALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
