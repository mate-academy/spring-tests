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
    private static final String NOT_EXISTING_USER = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername(NOT_EXISTING_USER);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
