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
    private static final String VALID_EMAIL = "user@i.ua";
    private static final String INVALID_EMAIL = "userSecond@i.ua";
    private static final String PASSWORD = "user1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final String USER_AUTHORITY = "ROLE_USER";
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_validEmail_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertTrue(actual.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(USER_AUTHORITY)));
    }

    @Test
    void loadUserByUsername_invalidEmail_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(INVALID_EMAIL),
                "Expected to receive UsernameNotFoundException");
    }
}
