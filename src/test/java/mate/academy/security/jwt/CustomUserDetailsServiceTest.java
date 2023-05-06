package mate.academy.security.jwt;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

class CustomUserDetailsServiceTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345678";

    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final String USER_AUTHORITY = "ROLE_USER";
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = customUserDetailsService.loadUserByUsername(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.getUsername());
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
        Assertions.assertEquals(USER_AUTHORITY, actual.getAuthorities()
                .stream()
                .toList()
                .get(0)
                .toString());
    }
}
