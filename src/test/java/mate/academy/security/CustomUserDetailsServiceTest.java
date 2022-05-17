package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.service.UserService;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    private UserService userService;
    private static final String CORRECT_EMAIL = "dana.khromenko@gmail.com";
    private static final String INVALID_EMAIL = "qwe@.ew";
    private static final String PASSWORD = "2417451";
    private static final String USER_NOT_FOUND_EXCEPTION_TEXT = "User not found.";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User expected = new User();
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(expected));

        UserDetails actual = userDetailsService.loadUserByUsername(CORRECT_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CORRECT_EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {

        User expected = new User();
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(expected));

        try {
            userDetailsService.loadUserByUsername(INVALID_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(USER_NOT_FOUND_EXCEPTION_TEXT, e.getMessage());
            return;
        }
        Assertions.fail("UsernameNotFoundException must be thrown");
    }
}
