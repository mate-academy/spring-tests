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
    private static final String DEFAULT_EMAIL = "mark.test@i.ua";
    private static final String DEFAULT_PASSWORD = "12345678";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private User user;
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void loadUserByUsername_defaultUser_ok() {
        Mockito.when(userService.findByEmail(DEFAULT_EMAIL))
                .thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(DEFAULT_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(DEFAULT_EMAIL, actual.getUsername());
        Assertions.assertEquals(DEFAULT_PASSWORD, actual.getPassword());
        Assertions.assertTrue(actual.getAuthorities().toString()
                .contains(USER_ROLE.getRoleName().name()));
    }

    @Test
    void loadUserByUsername_nonExistingEmail_notOk() {
        String nonExistingUsername = "nonExistingemail@i.ua";
        Mockito.when(userService.findByEmail(nonExistingUsername))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistingUsername));
    }

    @Test
    void loadUserByUsername_null_notOk() {
        Mockito.when(userService.findByEmail(null))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }
}
