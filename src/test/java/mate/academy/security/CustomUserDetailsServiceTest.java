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
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User bob;
    private String email = "bob@i.ua";
    private String password = "1234";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(actual.getPassword(), bob.getPassword());
    }

    @Test
    void loadUserByUsername_NotOk() {
        Mockito.when(userService.findByEmail(null)).thenThrow(UsernameNotFoundException.class);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));
    }
}
