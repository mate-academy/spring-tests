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
    public static final String EMAIL = "bob@gmail.com";
    public static final String PASSWORD = "1234";
    public static final int AUTHORITIES_QUANTITY = 1;
    public static final String MESSAGE = "User not found.";
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
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(AUTHORITIES_QUANTITY, actual.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("alice@gmail.com");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(MESSAGE, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
