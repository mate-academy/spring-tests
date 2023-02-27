package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "qwerty";
    private static final String NOT_EXISTENT_USER_EMAIL = "alice@i.ua";
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static User existentUser;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        existentUser = new User();
        existentUser.setEmail(USER_EMAIL);
        existentUser.setPassword(USER_PASSWORD);
        existentUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_existentUserEmail_Ok() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(existentUser));
        UserDetails userDetailsByUserName = userDetailsService.loadUserByUsername(USER_EMAIL);
        Assertions.assertNotNull(userDetailsByUserName);
        Assertions.assertEquals(USER_EMAIL, userDetailsByUserName.getUsername());
        Assertions.assertEquals(USER_PASSWORD, userDetailsByUserName.getPassword());
    }

    @Test
    void loadUserByUsername_notExistentUserEmail_usernameNotFoundException() {
        Mockito.when(userService.findByEmail(NOT_EXISTENT_USER_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(NOT_EXISTENT_USER_EMAIL));
    }
}
