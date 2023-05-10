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
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static String expectedEmail;
    private static User expectedUser;
    private static String expectedPassword;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        expectedEmail = "vitaliy@i.ua";
        expectedPassword = "12345678";
        expectedUser = new User();
        expectedUser.setPassword(expectedPassword);
        expectedUser.setEmail(expectedEmail);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_ok() {
        Mockito.when(userService.findByEmail(expectedEmail)).thenReturn(Optional.of(expectedUser));
        UserDetails actual = userDetailsService.loadUserByUsername(expectedEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedEmail, actual.getUsername());
        Assertions.assertEquals(expectedPassword, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound_notOK() {
        String expectedMessage = "User not found.";
        Mockito.when(userService.findByEmail(expectedEmail)).thenReturn(Optional.of(expectedUser));
        try {
            userDetailsService.loadUserByUsername("someoneUser");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(expectedMessage, e.getMessage(),
                    "Exception should thrown correct message");
            return;
        }
        Assertions.fail("Method should thrown UsernameNotFoundException class");
    }
}
