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
    private static final String USER_EMAIL = "bob123@gmail.com";
    private static final String USER_PASSWORD = "bob123";
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = customUserDetailsService.loadUserByUsername(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getUsername());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsernameWrongEmail_NotOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("wrongEmail"));
    }
}
