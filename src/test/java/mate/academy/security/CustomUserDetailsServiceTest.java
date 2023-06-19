package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String INCORRECT_USER_EMAIL = "incorrect.email";
    private static final String USER_PASSWORD = "password";
    private static final String INCORRECT_USER_PASSWORD = "incorrect.password";
    private static final Role.RoleName USER = Role.RoleName.USER;
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(USER);
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(role));
        userService = mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadByUserNameSuccess() {

        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_EMAIL);
        assertNotNull(actual);
        assertEquals(USER_EMAIL, actual.getUsername());
        assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void loadByUserNameException() {
        user.setEmail(INCORRECT_USER_EMAIL);
        user.setPassword(INCORRECT_USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        when(userService.findByEmail(INCORRECT_USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,() ->
                userDetailsService.loadUserByUsername(INCORRECT_USER_EMAIL),
                "Expected UserNotFoundException");
    }
}
