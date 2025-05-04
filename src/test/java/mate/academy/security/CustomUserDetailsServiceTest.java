package mate.academy.security;

import static mate.academy.model.Role.RoleName.USER;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private static UserService userService;
    private static CustomUserDetailsService userDetailsService;
    private static final String TEST_EMAIL = "some.name@test.test";

    @BeforeAll
    static void beforeAll() {
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validEmail_ok() {
        String password = "123456";
        Set<Role> roles = Set.of(new Role(USER));
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(password);
        user.setRoles(roles);
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_EMAIL);
        Assertions.assertEquals(TEST_EMAIL, userDetails.getUsername(),
                "Method should return userDetails with userName matching email passed");
        Assertions.assertEquals(password, userDetails.getPassword(),
                "Method should return userDetails with password matching password passed");
        String expectedRole = "ROLE_USER";
        String actualRole = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();
        Assertions.assertEquals(expectedRole, actualRole,
                "Method should return userDetails with authority matching role of user");
    }

    @Test
    void loadUserByUsername_newEmail_notOk() {
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(TEST_EMAIL),
                "Method should throw %s when invalid email is passed"
                        .formatted(UsernameNotFoundException.class));
    }
}
