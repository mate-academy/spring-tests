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
    private static final String USER_PASSWORD = "12345678";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getUsername());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());

    }

    @Test
    void loadUserByUsername_WrongEmail_NotOk() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(bob));
        try {
            userDetailsService.loadUserByUsername(null);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(e.getMessage(), "User not found.");
        }
    }
}
