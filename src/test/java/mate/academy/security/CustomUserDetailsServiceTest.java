package mate.academy.security;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private User user;
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        user.setPassword(PASSWORD);
    }

    @Test
    void loadUserByUsername_Ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("bob@@i.ua1"));
    }
}
