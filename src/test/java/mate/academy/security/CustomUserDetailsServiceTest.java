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
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static User bob;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bob = new User();
        bob.setEmail(VALID_EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.of(bob));
    }

    @Test
    void loadUserByUserName_ok() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals("1234", actual.getPassword());
    }

    @Test
    void loadUserByUserName_nonExistingUser_notOk() {
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        UsernameNotFoundException thrown
                = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
                    userDetailsService.loadUserByUsername(INVALID_EMAIL);
                });
        Assertions.assertEquals("User not found.", thrown.getMessage(),
                "Expected to throw UsernameNotFoundException with message: "
                        + "\"User not found.\"");
    }
}
