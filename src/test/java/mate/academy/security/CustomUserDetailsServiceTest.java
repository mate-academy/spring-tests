package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private static final String EMAIL = "test@ukr.net";
    private static final String PASSWORD = "12345";
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_existedEmail_ok() {
        User testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(new Role(USER)));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        UserDetails actual = customUserDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual, "method should not return null with valid User email");
        assertEquals(EMAIL, actual.getUsername(),
                "method should return UserDetails with saved in database email");
        assertEquals(PASSWORD, actual.getPassword(),
                "method should return UserDetails with saved in database password");
    }

    @Test
    void loadUserByUsername_notExistedEmail_ok() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(EMAIL),
                "method should throw UsernameNotFoundException if "
                        + "email incorrect or absent in database");
    }
}
