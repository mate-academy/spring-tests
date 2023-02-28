package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@e.mail";
    private static final String PASSWORD = "qwerty";
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(EMAIL, PASSWORD, Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_notFound_notOk() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertEquals("User not found.",
                Assertions.assertThrows(UsernameNotFoundException.class,
                        () -> userDetailsService.loadUserByUsername(EMAIL)).getMessage());
    }
}
