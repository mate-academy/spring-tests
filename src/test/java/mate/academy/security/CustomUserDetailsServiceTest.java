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
    private static final String EMAIL = "testmail@i.ua";
    private static final String PASSWORD = "12345";
    private static final long ID = 1L;
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);

        user = new User(ID, EMAIL, PASSWORD, ROLES);
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUsername_UserNotFound() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("falded@email");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
