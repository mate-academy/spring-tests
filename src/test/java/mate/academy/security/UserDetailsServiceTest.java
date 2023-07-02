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
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceTest {
    private static UserService userService;
    private static CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadByUsername_ok() {
        User user = new User();
        user.setEmail("test123@testmail.net");
        user.setPassword("test123");
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        Mockito.when(
                userService.findByEmail("test123@testmail.net")).thenReturn(Optional.of(user));
        UserBuilder builder;
        builder = withUsername("test123@testmail.net");
        builder.password(user.getPassword());
        builder.roles(user.getRoles()
                .stream()
                .map(r -> r.getRoleName().name())
                .toArray(String[]::new));
        UserDetails expected = builder.build();
        UserDetails actual
                = customUserDetailsService.loadUserByUsername("test123@testmail.net");
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void loadByUsername_notOk() {
        Mockito.when(userService.findByEmail("test123@testmail.net"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("test123@testmail.net"));
    }

    @Test
    void loadByUsername_null() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }
}
