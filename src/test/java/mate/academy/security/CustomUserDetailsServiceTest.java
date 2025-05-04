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
    private static final String BOB_EMAIL = "bob@i.ua";
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService =
                new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(BOB_EMAIL);
        user.setPassword("12345678");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(BOB_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual =
                userDetailsService.loadUserByUsername(BOB_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_EMAIL, actual.getUsername());
        Assertions.assertEquals("12345678", actual.getPassword());
    }

    @Test
    void loadUserByUsername_InvalidEmail_NotOk() {
        String email = "tom@com.ua";
        User tom = new User();
        tom.setEmail(email);
        tom.setPassword("1234");
        tom.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(tom));

        try {
            userDetailsService.loadUserByUsername(BOB_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
