package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validData_shouldReturnValidUserDetails() {
        String email = "denik@gmail.com";
        String password = "password";
        final UserDetails expected = User.withUsername(email)
                .password(password)
                .roles("USER")
                .build();
        mate.academy.model.User user = new mate.academy.model.User();
        user.setEmail(email);
        user.setPassword(password);
        user.setId(1L);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void loadUserByUsername_unValidData_shouldThrowUsernameNotFoundException() {
        String email = "denik@gmail.com";
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
