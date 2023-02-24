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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bobik@g.com";
    private static final String PASSWORD = "1234567890";
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertEquals(userDetails.getClass(),
                org.springframework.security.core.userdetails.User.class);
        Assertions.assertTrue(userDetails.isAccountNonExpired());
        Assertions.assertEquals(userDetails.getUsername(), user.getEmail());
        Assertions.assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        Assertions.assertThrows(UsernameNotFoundException.class, ()
                -> customUserDetailsService.loadUserByUsername(null));
    }
}
