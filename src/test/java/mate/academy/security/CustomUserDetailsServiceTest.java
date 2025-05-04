package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "tomas@i.ua";
    private static final String PASSWORD = "1234";
    private static final Role ROLE_OF_TOMAS = new Role(Role.RoleName.USER);
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE_OF_TOMAS));
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
    void loadUserByUsername_UsernameNotFoundException() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
