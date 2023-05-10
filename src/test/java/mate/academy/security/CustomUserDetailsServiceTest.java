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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "tom@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String WRONG_EMAIL = "alice@gmail.com";
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = loadUserForTest();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(userDetails.getUsername(), user.getEmail());
        Assertions.assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        User user = loadUserForTest();
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        try {
            customUserDetailsService.loadUserByUsername(WRONG_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }

    @Test
    void loadUserByUserName_NullEmail_NotOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }

    private User loadUserForTest() {
        User test = new User();
        test.setEmail(EMAIL);
        test.setPassword(PASSWORD);
        test.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userService.save(test);
        return test;
    }
}
