package mate.academy.security;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "actual@mail.ua";
    private static final String PASSWORD = "123456";
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("wrong@mail.com"));
    }

    @Test
    void loadUserByUsername_emailNull_notOk() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));
        ;
    }
}
