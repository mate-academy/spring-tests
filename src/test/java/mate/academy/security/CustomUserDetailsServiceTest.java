package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private static final String CORRECT_EMAIL = "bob@i.ua";
    private static final String FAKE_EMAIL = "fake@i.ua";
    private static final String PASSWORD = "1234";
    @Mock
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(CORRECT_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(CORRECT_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CORRECT_EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_NotOk() {
        UsernameNotFoundException exception = Assertions
                .assertThrows(UsernameNotFoundException.class,
                        () -> userDetailsService.loadUserByUsername(FAKE_EMAIL));
        Assertions.assertEquals("User not found.", exception.getMessage());
    }
}
