package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    @Mock
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new CustomUserDetailsService(userService);
        expectedUser = new User();
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
        expectedUser.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails user = userDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertEquals(user.getUsername(), EMAIL);
        Assertions.assertEquals(user.getPassword(), PASSWORD);
        Assertions.assertNotNull(user);
    }

    @Test
    void loadUserByUsername_usernameNull_notOk() {
        Mockito.when(userService.findByEmail(null)).thenThrow(UsernameNotFoundException.class);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null));
    }
}