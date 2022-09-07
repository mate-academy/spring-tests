package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_OK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getUsername());
        assertEquals("1234", actual.getPassword());
    }

    @Test
    void loadUserByUsername_emptyUser_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(user.getEmail()));
    }
}
