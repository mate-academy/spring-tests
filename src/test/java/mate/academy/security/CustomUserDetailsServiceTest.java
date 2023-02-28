package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest {
    private static final String BOB_VALID_EMAIL = "bob@mail.com";
    private static final String BOB_INVALID_EMAIL = "bb@mail.com";
    private static final String SIMPLE_NUMBER_PASSWORD = "12345";
    private static CustomUserDetailsService userDetailsService;
    private static UserService userService;
    private static User bob;
    private static Role userRole;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        bob = new User();
        bob.setEmail(BOB_VALID_EMAIL);
        bob.setPassword(SIMPLE_NUMBER_PASSWORD);
        bob.setRoles(Set.of(userRole));

        Mockito.when(userService.findByEmail(BOB_VALID_EMAIL)).thenReturn(Optional.of(bob));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(BOB_VALID_EMAIL);
        assertEquals(bob.getEmail(), actual.getUsername(),
                "You must return valid user, when you got valid email");
    }

    @Test
    void loadUserByUsername_NotOk() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(BOB_INVALID_EMAIL),
                "When you get invalid username you must throw exception");
    }
}
