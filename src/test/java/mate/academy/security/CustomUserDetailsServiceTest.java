package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static User testUser;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
    }

    @Test
    void loadUserByUsername_existingUser_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(testUser.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getUsername());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_usernameNotFoundException() {
        try {
            userDetailsService.loadUserByUsername(INVALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to recieve UsernameNotFoundException");
    }
}
