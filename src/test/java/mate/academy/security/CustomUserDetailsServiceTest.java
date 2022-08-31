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
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private static final String ALICE_USER_EMAIL = "alice@i.ua";
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    public void loadUserByUserName_ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    public void loadUserByUserName_nonExistentLogin_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername(ALICE_USER_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(ALICE_USER_EMAIL));
    }
}
