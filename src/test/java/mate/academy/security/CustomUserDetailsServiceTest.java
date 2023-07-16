package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String TEST_EMAIL = "test@mail.com";
    private static final String TEST_PASSWORD = "pass";
    private static final Role TEST_ROLE = new Role(Role.RoleName.USER);

    private final UserService userService = mock(UserService.class);
    private final UserDetailsService userDetailsService = new CustomUserDetailsService(userService);

    @Test
    void loadUserByUsername_validEmailAndUserFound_ok() {
        User found = new User();
        found.setId(1L);
        found.setEmail(TEST_EMAIL);
        found.setPassword(TEST_PASSWORD);
        found.setRoles(Set.of(TEST_ROLE));
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(found));

        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_EMAIL);
        assertEquals(TEST_EMAIL, userDetails.getUsername());
        assertEquals(TEST_PASSWORD, userDetails.getPassword());
        assertIterableEquals(
                Set.of(new SimpleGrantedAuthority("ROLE_" + TEST_ROLE.getRoleName().name())),
                userDetails.getAuthorities()
        );
    }

    @Test
    void loadUserByUsername_validEmailAndUserNotFound_notOk() {
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(TEST_EMAIL)
        );
    }

    @Test
    void loadUserByUsername_emailIsNull_notOk() {
        when(userService.findByEmail(null)).thenReturn(Optional.empty());
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(TEST_EMAIL)
        );
    }
}
