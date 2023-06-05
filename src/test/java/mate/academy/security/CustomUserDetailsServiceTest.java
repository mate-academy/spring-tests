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
    private static final String VALID_EMAIL = "bob@bob.ua";
    private static final String INVALID_EMAIL = "not-bob@bob.ua";
    private static final String VALID_PASSWORD = "12345678";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setPassword(VALID_PASSWORD);
        user.setEmail(VALID_EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.ofNullable(user));
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_InvalidEmail_NotOk() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.ofNullable(user));
        try {
            userDetailsService.loadUserByUsername(INVALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "User not found.");
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException ");
    }
}
