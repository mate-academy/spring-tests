package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        String email = "denis@gmail.com";
        User den = new User();
        den.setEmail(email);
        den.setPassword("77777777");
        den.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(den));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals("77777777", actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        UsernameNotFoundException exception =
                Assertions.assertThrows(UsernameNotFoundException.class, () -> {
                    userDetailsService.loadUserByUsername("denis1@gmail.com");
                }, "UsernameNotFoundException was expected");
        Assertions.assertEquals("User not found.", exception.getMessage());
    }
}
