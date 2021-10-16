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
    private static final String EMAIL = "robokop@mail.com";
    private static final String PASSWORD = "12345";
    private User user;
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadByUserName_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
    }
}
