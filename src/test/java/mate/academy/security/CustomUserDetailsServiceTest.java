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
    private static final String EMAIL = "bob@com.ua";
    private static final String PASSWORD = "1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserService userService;
    private CustomUserDetailsService userDetailsService;
    private User bob;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bob = new User();
        bob.setId(1L);
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UserNameNotFound_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("alice@com.ua");
        });
    }
}
