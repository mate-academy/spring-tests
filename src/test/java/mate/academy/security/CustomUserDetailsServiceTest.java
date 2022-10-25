package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "user@email.com";
    private static final String INVALID_EMAIL = "Invalid";
    private static CustomUserDetailsService customUserDetailsService;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = createUser();
    }

    @Test
    public void loadUserByUsername_ValidEmail_Ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(user.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_InvalidEmail_Ok() {
        Mockito.when(userService.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(INVALID_EMAIL),
                "UsernameNotFoundException to be thrown, but nothing was thrown");
    }

    private static User createUser() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword("user1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
