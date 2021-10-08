package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static User userBob;
    private static User userAlice;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        userBob = new User();
        userBob.setEmail("bob@i.ua");
        userBob.setPassword("1234");
        userBob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userAlice = new User();
        userAlice.setEmail("alice@i.ua");
        userAlice.setPassword("4321");
        userAlice.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(userBob.getEmail())).thenReturn(Optional.of(userBob));

        UserDetails actual = userDetailsService.loadUserByUsername(userBob.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userBob.getEmail(), actual.getUsername());
        Assertions.assertEquals(userBob.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound() {
        Mockito.when(userService.findByEmail(userBob.getEmail())).thenReturn(Optional.of(userBob));

        try {
            userDetailsService.loadUserByUsername(userAlice.getEmail());
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
