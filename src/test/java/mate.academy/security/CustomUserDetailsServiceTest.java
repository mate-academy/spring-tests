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

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "robokop@mail.com";
    private static final String PASSWORD = "12345";
    private static final String NON_EXISTING_EMAIL = "neo@mail.com";
    private static final String  NON_EXISTING_PASSWORD = "777";
    private static User user;
    private static CustomUserDetailsService customUserDetailsService;
    private static UserService userService;
    private static User nonExistingUser;

    @BeforeAll
    static void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        nonExistingUser = new User();
        nonExistingUser.setPassword(NON_EXISTING_PASSWORD);
        nonExistingUser.setEmail(NON_EXISTING_EMAIL);
    }

    @Test
    void loadByUserName_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
    }

    @Test
    void loadByUserName_NotOk() {
        Mockito.when(userService.findByEmail(NON_EXISTING_EMAIL)).thenReturn(Optional.of(nonExistingUser));
        Assertions.assertThrows(Exception.class, () ->
                customUserDetailsService.loadUserByUsername(NON_EXISTING_EMAIL)
        );
    }
}
