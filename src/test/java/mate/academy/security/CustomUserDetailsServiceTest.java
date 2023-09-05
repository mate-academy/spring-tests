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
    private final static String email = "bob@gmail.ua";
    private final static String password = "1234";
    private User user;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(email))
                .thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_notOk() {
        Mockito.when(userService.findByEmail("incorrect_email"))
                .thenReturn(Optional.of(user));
        try {
            userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException exception) {
            Assertions.assertEquals("User not found.", exception.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
