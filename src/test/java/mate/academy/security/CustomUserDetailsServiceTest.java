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
    private static final String LOGIN = "john@ttt.com";
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
        user.setPassword(PASSWORD);
        user.setEmail(LOGIN);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(LOGIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(LOGIN, user.getEmail());
        Assertions.assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String email = "alise@gmail.com";
        User user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(LOGIN);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());

        }
        Assertions.fail("Expected to receive UsernameNotFoundException ");
    }
}
