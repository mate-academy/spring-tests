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

public class CustomUserDetailsServiceTest {
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;
    private User user;
    private Role role;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails result = customUserDetailsService.loadUserByUsername(user.getEmail());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result.getUsername());
        Assertions.assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_notOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(user.getEmail());
        });
    }
}
