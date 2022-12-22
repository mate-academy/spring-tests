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
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(USER_LOGIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_LOGIN, actual.getUsername());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        Mockito.when(userService.findByEmail(USER_LOGIN)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(USER_LOGIN));
    }
}
