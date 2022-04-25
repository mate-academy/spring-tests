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
    private String userEmail;
    private String userPassword;
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        userEmail = "user@gmail.com";
        userPassword = "12345";
        user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userEmail, actual.getUsername());
        Assertions.assertEquals(userPassword, actual.getPassword());
    }

    @Test
    void loadUserByUsername_nonExistentUsername_Ok() {
        Mockito.when(userService.findByEmail(userEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(userEmail),
                "Expected UsernameNotFoundException when User email doesn't exist");
    }
}
