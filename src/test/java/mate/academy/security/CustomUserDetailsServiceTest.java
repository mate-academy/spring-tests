package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "def@vijd.com";
    private static final String PASSWORD = "pass";
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_unexisting_notOk() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(EMAIL));
    }

    @Test
    void loadUserByUsername_existing_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of());
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername(EMAIL);
        } catch (Exception e) {
            fail("Expected to load user details without errors for valid existing user");
        }
    }
}
