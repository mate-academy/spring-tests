package mate.academy.security;

import static org.springframework.security.core.userdetails.User.withUsername;

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
    private static final String LOGIN = "test@email.com";
    private static final String PASSWORD = "1234";
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(LOGIN);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    public void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.of(user));
        org.springframework.security.core.userdetails.User.UserBuilder builder =
                withUsername(LOGIN);
        builder.password(user.getPassword());
        builder.roles(user.getRoles()
                .stream()
                .map(x -> x.getRoleName().name())
                .toArray(String[]::new));
        UserDetails expected = builder.build();
        UserDetails actual = userDetailsService.loadUserByUsername(LOGIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void loadUserByUsername_NotOk() {
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.empty());
        UsernameNotFoundException exception =
                Assertions.assertThrows(UsernameNotFoundException.class,
                        () -> userDetailsService.loadUserByUsername(LOGIN));
        Assertions.assertEquals("User not found.", exception.getMessage());
    }
}
