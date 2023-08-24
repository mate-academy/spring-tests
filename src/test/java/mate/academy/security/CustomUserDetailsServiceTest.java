package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private static final String EMAIL = "mail@gmail.com";
    private static final String PASSWORD = "1234";
    private User user;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);
    }

    @Test
    void loadUserByUsername_OK() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_Not_OK() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL));
    }
}
