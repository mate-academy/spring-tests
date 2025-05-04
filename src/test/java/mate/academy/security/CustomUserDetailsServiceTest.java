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
    private static final String EMAIL = "bob@i.ua";
    private static final String EMAIL_IS_NOT_IN_DB = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_userIsNotInDb_ok() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL_IS_NOT_IN_DB));
    }
}
