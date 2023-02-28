package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "user@i.ua";
    private static final String PASSWORD = "2345";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        assertTrue(actual
                .getAuthorities()
                .toString()
                .matches("(.*)" + ROLE.getRoleName().name() + "(.*)"));
    }

    @Test
    void loadUserByUsername_NoSuchUsername_NotOk() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("alice@i.ua"));
    }

    @Test
    void loadUserByUsername_NullUsername_NotOk() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));
    }

    @Test
    void loadUserByUsername_EmptyUsername_NotOk() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(""));
    }
}
