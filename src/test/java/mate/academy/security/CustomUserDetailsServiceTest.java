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
    private User bob;
    private String bobPassword;
    private String bobEmail;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        bobEmail = "bob@i.ua";
        bobPassword = "bob54321";
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        bob = new User();
        bob.setPassword(bobPassword);
        bob.setEmail(bobEmail);
        bob.setRoles(Set.of(userRole));
    }

    @Test
    void loadUserByUsername_correctUsername_isOk() {
        Mockito.when(userService.findByEmail(bobEmail)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(bobEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bobEmail, actual.getUsername());
        Assertions.assertEquals(bobPassword, actual.getPassword());
    }

    @Test
    void loadUserByUsername_notExistEmail_throwUsernameNotFoundException() {
        Mockito.when(userService.findByEmail(bobEmail)).thenReturn(Optional.of(bob));
        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException ex) {
            Assertions.assertEquals("User not found.", ex.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
