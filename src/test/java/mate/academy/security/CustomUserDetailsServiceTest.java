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

    private final String userName = "bob@i.ua";
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        User userBob = new User();
        userBob.setEmail(userName);
        userBob.setPassword("1234");
        userBob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Optional<User> userOptional = Optional.of(userBob);
        Mockito.when(userService.findByEmail(userName)).thenReturn(userOptional);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(userBob.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(userBob.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_notExistName_notOk() {
        Optional<User> userOptional = Optional.empty();
        Mockito.when(userService.findByEmail(userName)).thenReturn(userOptional);
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }

}
