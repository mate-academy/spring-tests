package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserService userService = Mockito.mock(UserService.class);
    private UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
    private UserDetails expectedUserDetails;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        role = new Role(Role.RoleName.USER);
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(role));
        expectedUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(TEST_EMAIL_OK).password(TEST_PASSWORD_OK)
                .roles(role.getRoleName().name()).build();
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(TEST_EMAIL_OK)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(TEST_EMAIL_OK);
        assertNotNull(actual);
        assertEquals(expectedUserDetails, actual);
    }

    @Test
    void loadUserByUsername_Not_Ok() {
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("Wrong email"));
    }
}
