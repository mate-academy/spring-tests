package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static User user;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        role = new Role();
        role.setRoleName(ROLE);
    }

    @BeforeEach
    void setUp() {
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual, "Method must return UserDetails object");
        Assertions.assertEquals(EMAIL, actual.getUsername(),
                "Expected " + EMAIL + ", but was " + actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword(),
                "Expected " + PASSWORD + ", but was " + actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername("anotheremail@gmail.com");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
