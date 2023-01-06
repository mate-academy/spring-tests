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
    private static final String USER_NAME = "email";
    private static final String USER_PASSWORD = "password";
    private static final Set<Role> USER_ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User(USER_NAME, USER_PASSWORD, USER_ROLES);
        Mockito.when(userService.findByEmail(USER_NAME)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_NotExistUserName_Exception_NotOk() {
        Mockito.when(userService.findByEmail(USER_NAME)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(USER_NAME);
        });
    }
}
