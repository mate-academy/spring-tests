package mate.academy.security;

import java.util.Optional;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User.UserBuilder;
public class UserDetailsServiceTest {
    private UserService userService;
    private  CustomUserDetailsService customUserDetailsService;

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
        Mockito.when(userService.findByEmail("test123@testmail.net")).thenReturn(Optional.of(user));
        UserDetails userDetails
                = customUserDetailsService.loadUserByUsername("test123@testmail.net");

        UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername("test123@testmail.net");

    }
}
