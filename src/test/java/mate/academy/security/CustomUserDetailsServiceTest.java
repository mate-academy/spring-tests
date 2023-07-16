package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345";
    private static final String[] ROLES = new String[] {"USER"};
    private final UserService userService = mock(UserService.class);
    private final CustomUserDetailsService userDetailsService =
            new CustomUserDetailsService(userService);
    private UserDetails userDetails;
    private User.UserBuilder userBuilder;
    private mate.academy.model.User user;

    @BeforeEach
    void setUp() {
        user = new mate.academy.model.User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        userBuilder = User
                .withUsername(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .roles(ROLES);

        userDetails = userBuilder.build();
    }

    @Test
    void loadUserByUsername_Ok() {
        when(userService.findByEmail(any()))
                .thenReturn(Optional.ofNullable(user));

        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);

        assertNotNull(actual);
        assertEquals(userDetails, actual);
    }

    @Test
    void loadUserByUsername_notOk() {
        when(userService.findByEmail(any()))
                .thenReturn((Optional.empty()));

        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }

        fail("Expected to receive UsernameNotFoundException");
    }
}
