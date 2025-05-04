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
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String NOT_EXIST_USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getUsername());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound_NotOk() {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        try {
            userDetailsService.loadUserByUsername(NOT_EXIST_USER_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
