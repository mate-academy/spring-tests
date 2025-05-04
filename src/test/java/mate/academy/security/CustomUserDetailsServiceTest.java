package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String RAW_PASSWORD = "password";
    private static UserService userService;

    private static CustomUserDetailsService customUserDetailsService;
    private Role userRole;
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(RAW_PASSWORD);
        user.setRoles(Set.of(userRole));
        user.setId(1L);
    }

    @Test
    void loadUserByUsername_correctData_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        assertEquals(EMAIL, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_emptyOptional_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
        }
    }
}
