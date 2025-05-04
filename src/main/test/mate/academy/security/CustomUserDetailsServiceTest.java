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
import org.springframework.security.core.userdetails.User.UserBuilder;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123456789";
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        user.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(EMAIL);
        builder.password(PASSWORD);
        builder.roles("USER");
        UserDetails expected = builder.build();

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
        Assertions.assertEquals(expected.getAuthorities(), actual.getAuthorities());
    }

    @Test
    void loadUserByUsernameEmptyUser() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        try {
            userDetailsService.loadUserByUsername(EMAIL);
        } catch (Exception e) {
            Assertions.assertEquals("User not found.", e.getMessage());
        }
    }
}
