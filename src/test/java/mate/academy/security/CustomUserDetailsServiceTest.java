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
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadByUsername_ok() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("11111111");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void loadByUsername_usernameNotFound_notOk() {
        Throwable exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("user@mail.com");
        }, "UsernameNotFoundException was expected");
        Assertions.assertEquals("User not found.", exception.getLocalizedMessage());
    }
}
