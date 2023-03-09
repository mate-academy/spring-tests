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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private static UserService userService;
    private static CustomUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validEmail_ok() {
        String email = "some@email.com";
        String password = "1234";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(email, userDetails.getUsername(),
                "Method should return userDetails with userName matching email passed\n");
        Assertions.assertEquals(password, userDetails.getPassword(),
                "Method should return userDetails with password matching password passed\n");
        String expectedRole = "ROLE_USER";
        String actualRole = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();
        Assertions.assertEquals(expectedRole, actualRole,
                "Method should return userDetails with authority matching role of user\n");
    }

    @Test
    void loadUserByUsername_invalidEmail_notOk() {
        String invalidEmail = "invalid@email.com";
        Mockito.when(userService.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(invalidEmail),
                "Method should throw %s when invalid email is passed"
                        .formatted(UsernameNotFoundException.class));
    }
}
