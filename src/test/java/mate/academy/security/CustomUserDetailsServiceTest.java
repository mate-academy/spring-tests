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

class CustomUserDetailsServiceTest {
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "1234";
    private static User test;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        test = new User();
        test.setEmail(EMAIL);
        test.setPassword(PASSWORD);
        test.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(test));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails loadUserByUsername = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(loadUserByUsername);
        Assertions.assertEquals(EMAIL,loadUserByUsername.getUsername());
        Assertions.assertEquals(PASSWORD,loadUserByUsername.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        try {
            userDetailsService.loadUserByUsername("test2@gmail.com");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(e.getMessage(),"User not found.");
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
