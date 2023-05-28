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
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validEmail_ok() {
        String expectedEmail = "bob@gmail.com";
        String expectedPassword = "somePassword";
        Role expectedRole = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(expectedEmail);
        user.setPassword(expectedPassword);
        user.setRoles(Set.of(expectedRole));
        Mockito.when(userService.findByEmail(expectedEmail)).thenReturn(Optional.of(user.clone()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(expectedEmail);

        Assertions.assertEquals(expectedEmail, userDetails.getUsername());
        Assertions.assertEquals(expectedPassword, userDetails.getPassword());
        Assertions.assertEquals("ROLE_" + expectedRole.getRoleName().name(),
                userDetails.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @Test
    void loadUserByUsername_invalidEmail_notOk() {
        Mockito.when(userService.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("email"));
    }
}
