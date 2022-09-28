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

public class CustomUserDetailsServiceTest {
    private static final String EMAIL = "modernboy349@gmail.com";
    private static final String PASSWORD = "Hello123";
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_WringUsername_NotOk() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Throwable exception = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {
                userDetailsService.loadUserByUsername("WRONG_EMAIL"); },
                "User not found.");
        Assertions.assertEquals("User not found.", exception.getLocalizedMessage());
    }
}
