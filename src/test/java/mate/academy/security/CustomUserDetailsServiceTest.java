package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String USER_EMAIL = "john@me.com";
    private static final String USER_PASSWORD = "12345678";
    private static final String NON_EXIST_USER = "mary@me.com";
    private static User user;
    private static UserService userService;
    private static CustomUserDetailsService customUserDetailsService;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);

        Role userRole = new Role(Role.RoleName.USER);

        user = new User();
        user.setRoles(Set.of(userRole));
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void loadUserByUsername_existUser_Ok() {
        Mockito.when(userService.findByEmail(USER_EMAIL))
                                .thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(USER_EMAIL);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles().size(), actual.getAuthorities().size(),
                "The number of roles is not equal");
        List<String> expectedRoles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        List<String> actualRoles = actual.getAuthorities().stream()
                .map(role -> role.getAuthority().replaceFirst("^ROLE_", ""))
                .toList();
        Assertions.assertTrue(expectedRoles.containsAll(actualRoles));
    }

    @Test
    void loadUserByUsername_nonExistUser_notOk() {
        Mockito.when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(NON_EXIST_USER),
                "Expected to receive UsernameNotFoundException");
    }
}
