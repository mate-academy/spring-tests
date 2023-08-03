package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String BOB_EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final long BOB_ID = 1L;
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private static final String ALICE_EMAIL = "alice@gmail.com";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User(BOB_ID, BOB_EMAIL, PASSWORD, ROLES);
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(BOB_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(BOB_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUsername_userNotFound_notOk() {
        Mockito.when(userService.findByEmail(ALICE_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(ALICE_EMAIL),
                "Expected to receive UserNotFoundException");
    }
}
