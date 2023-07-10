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
    private static final String PASSWORD = "1234";
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = getUser();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        User user = new User();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("bob@@i.ua1");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("expected to receive UsernameNotFoundException");
    }

    private User getUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        user.setPassword(PASSWORD);
        return user;
    }
}
