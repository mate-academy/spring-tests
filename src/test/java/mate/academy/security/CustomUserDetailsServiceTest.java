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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_correctEmail_ok() {
        String email = "bob@gmail.com";
        User bob = new User();
        bob.setEmail(email);
        bob.setPassword("1234");
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        bob.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));

        UserDetails actual = userDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual, "Result must be not null");
        Assertions.assertEquals(email, actual.getUsername(), "Expected " + email + " but actual");
        Assertions.assertEquals("1234", actual.getPassword());

    }

    @Test
    void loadUserByUsername_UserNotFoundException_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("alice@gmail.com"),
                "If an user not found, method should throw UsernameNotFoundException");
    }
}
