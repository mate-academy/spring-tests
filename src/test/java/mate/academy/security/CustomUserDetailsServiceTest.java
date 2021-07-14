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

class CustomUserDetailsServiceTest {
    private UserService userService;
    private CustomUserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setId(1L);
        user.setEmail("lucy1234@gmail.com");
        user.setPassword("12345tyr");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_ok() {
        Mockito.when(userService.findByEmail("lucy1234@gmail.com")).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername("lucy1234@gmail.com");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
    }
}