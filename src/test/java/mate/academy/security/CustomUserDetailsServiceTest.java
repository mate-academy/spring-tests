package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String USER_EMAIL = "test@com.ua";
    private static final String USER_PASSWORD = "password";
    private static final String EMAIL_NON_EXIST = "nonexist@com.ua";
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void setUp() {
        userService = mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_EMAIL);
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getUsername());
        assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UserNameNotFound() {
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername(EMAIL_NON_EXIST);
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("Expected to receive UserNotFoundException");
    }
}
