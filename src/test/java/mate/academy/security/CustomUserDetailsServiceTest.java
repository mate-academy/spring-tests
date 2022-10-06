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
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private static final int ROLES_QUANTITY = 1;
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void loadUserByUsername_ValidData_Ok() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(USER_LOGIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(ROLES_QUANTITY, actual.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_UsernameNotFound_NotOk() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                        userDetailsService.loadUserByUsername(user.getEmail()));
    }
}
