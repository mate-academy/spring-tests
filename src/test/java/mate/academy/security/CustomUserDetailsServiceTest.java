package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private static final String EMAIL = "bob@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(ROLES);
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_userNotFound_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername(EMAIL);
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
