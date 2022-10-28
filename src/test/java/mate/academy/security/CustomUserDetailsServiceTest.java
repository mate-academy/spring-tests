package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.core.userdetails.User.withUsername;

public class CustomUserDetailsServiceTest {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    public void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        org.springframework.security.core.userdetails.User.UserBuilder builder = withUsername(EMAIL);
        builder.password(user.getPassword());
        builder.roles(user.getRoles()
                .stream()
                .map(x -> x.getRoleName().name())
                .toArray(String[]::new));
        UserDetails expected = builder.build();
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void loadUserByUsername_NotOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL));
        assertEquals("User not found.", exception.getMessage());
    }
}
