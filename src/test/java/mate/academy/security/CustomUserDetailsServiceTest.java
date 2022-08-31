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
    private static final String EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "bobick@gmail.com";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_ok() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_invalidEmail_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername(INVALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
