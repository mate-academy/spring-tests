package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

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

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@mail.com";
    private static final String WRONG_EMAIL = "wrongEmail@mail.com";
    private static final String PASSWORD = "1234";
    private static UserService userService;
    private static CustomUserDetailsService customUserDetailsService;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User bob = createTestUser();
        Mockito.when(userService.findByEmail(any(String.class))).thenReturn(Optional.of(bob));
        UserDetails actual = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.getUsername());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        User bob = createTestUser();
        Mockito.when(userService.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        try {
            UserDetails actual = customUserDetailsService.loadUserByUsername(WRONG_EMAIL);
        } catch (Exception e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
