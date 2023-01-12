package mate.academy.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;

class CustomUserDetailsServiceTest {
    private static final Role ADMIN_ROLE = new Role(Role.RoleName.ADMIN);
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "12345";
    private static final Set<Role> ROLES = Set.of(ADMIN_ROLE);
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);
    }

    @Test
    void loadUserByUsername_Ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual, "User must not be null");
        assertEquals(user.getEmail(), actual.getUsername(),
                "Usernames don't match.");
        assertEquals(user.getPassword(), actual.getPassword(),
                "Passwords don't match.");
    }

    @Test
    void loadUserByUsername_UserNotFound_NotOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL));
    }
}