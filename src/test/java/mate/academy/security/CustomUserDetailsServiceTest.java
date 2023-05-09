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
    private static final String EMAIL = "bob@i.ui";
    private static final String PASSWORD = "1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        expectedUser = new User();
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
        expectedUser.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails user = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertEquals(user.getUsername(), EMAIL);
        Assertions.assertEquals(user.getPassword(), PASSWORD);
        Assertions.assertNotNull(user);
    }

    @Test
    void loadUserByUsername_usernameIsNull_Ok() {
        Mockito.when(userService.findByEmail(null)).thenThrow(UsernameNotFoundException.class);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));
    }
}
