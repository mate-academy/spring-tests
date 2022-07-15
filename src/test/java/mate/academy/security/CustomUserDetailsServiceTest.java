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
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "qwe123";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private User user;
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
    }

    @Test
    void loadUserByUsername_ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetails actual = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertTrue(actual.getAuthorities().toString()
                .contains(ROLE.getRoleName().name()));
    }

    @Test
    void loadUserByUsername_wrongUsername_notOk() {
        String wrongUsername = "wrongUsername@gmail.com";
        Mockito.when(userService.findByEmail(wrongUsername)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(wrongUsername));
    }

    @Test
    void loadUserByUsername_nullUsername_notOk() {
        Mockito.when(userService.findByEmail(null)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }
}
