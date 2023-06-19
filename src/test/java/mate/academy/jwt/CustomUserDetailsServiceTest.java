package mate.academy.jwt;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "valid@mail.com";
    private static final String INVALID_EMAIL = "invalid@mail.com";
    private static final String VALID_PASSWORD = "valid password";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUserEmail_Ok() {
        Mockito.when(userService.findByEmail(Mockito.eq(VALID_EMAIL)))
                .thenReturn(Optional.of(user));
        String actualUserMail = userDetailsService.loadUserByUsername(
                user.getEmail()).getUsername();
        Assertions.assertEquals(user.getEmail(), actualUserMail);
    }

    @Test
    void loadUserByUserEmail_invalidMail_notOk() {
        UsernameNotFoundException usernameNotFoundException =
                Assertions.assertThrows(UsernameNotFoundException.class,
                        () -> userDetailsService.loadUserByUsername(INVALID_EMAIL));
        Assertions.assertEquals("User not found.",
                usernameNotFoundException.getMessage());
    }
}
