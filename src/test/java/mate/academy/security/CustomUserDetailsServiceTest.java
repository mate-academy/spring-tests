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

public class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "bobTheBest";
    private User user;
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadByUsernameMethod_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual, "loadByUsername Method must return not null");
        Assertions.assertEquals(EMAIL, actual.getUsername(),
                "UserDetails object contains incorrect password");
        Assertions.assertEquals(PASSWORD, actual.getPassword(),
                "UserDetails object contains incorrect email");
    }

    @Test
    void loadByUsernameMethod_NotOk() {
        try {
            userDetailsService.loadUserByUsername("notbob@gmail.com");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNameNotFoundException with bad credentials");
    }
}
