package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private static final String EMAIL = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(USER)));
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validEmail_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername(),
                "Method should return userDetails with userName '%s' but returned '%s'"
                        .formatted(EMAIL, actual.getUsername()));
        assertEquals(PASSWORD, actual.getPassword(),
                "Method should return userDetails with password '%s' but returned '%s'"
                        .formatted(PASSWORD, actual.getPassword()));
        boolean ifRoleUserExist = actual.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals("ROLE_" + USER.name()));
        assertTrue(ifRoleUserExist,
                "Method should return userDetails with authority '%s'"
                        .formatted("ROLE_" + USER.name()));
    }

    @Test
    void loadUserByUsername_notExistedEmail_notOk() {
        String notExistingEmail = "not_exist@i.ua";
        lenient().when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(notExistingEmail),
                "Method should throw '%s' when not existing email '%s' is passed"
                        .formatted(UsernameNotFoundException.class, notExistingEmail));
        assertEquals("User not found.", exception.getMessage());
    }
}
