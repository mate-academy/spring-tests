package mate.academy.security;

import static org.mockito.ArgumentMatchers.eq;

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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        user.setPassword(PASSWORD);
        Mockito.when(userService.findByEmail(eq(EMAIL))).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(EMAIL, userDetails.getUsername());
        Assertions.assertEquals(PASSWORD, userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_invalidEmail_NotOk() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        user.setPassword(PASSWORD);
        Mockito.when(userService.findByEmail(eq(EMAIL))).thenReturn(Optional.of(user));
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("alice@i.ua"));
    }
}
