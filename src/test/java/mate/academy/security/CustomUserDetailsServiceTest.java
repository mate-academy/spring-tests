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
    private static final String VALID_EMAIL = "email@test.test";
    private static final String TEST_PASSWORD = "qwerty123";
    private static UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals(TEST_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonValidEmail@test.test"));
    }
}
