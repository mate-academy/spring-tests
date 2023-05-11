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
    private static final String USER_NAME = "bob@i.ua";
    private static final String USER_PASSWORD = "1234";
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(USER_NAME)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByName_ok() {
        UserDetails actual = customUserDetailsService.loadUserByUsername(USER_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_NAME, actual.getUsername());
    }

    @Test
    void loadUserByName_EmptyUserName_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(""));
    }
}
