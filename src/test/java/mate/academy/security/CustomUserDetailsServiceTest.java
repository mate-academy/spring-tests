package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String TEST_VALID_EMAIL = "bob@i.ua";
    private static final String TEST_INVALID_EMAIL = "alice@i.ua";
    private static final String TEST_PASSWORD = "1234";
    private static User testUser;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        testUser = new User();
        testUser.setEmail(TEST_VALID_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(TEST_VALID_EMAIL))
                .thenReturn(Optional.of(testUser));
    }

    @Test
    void loadUserByUsername_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(TEST_VALID_EMAIL);
        Assertions.assertNotNull(actual, "UserDetails can't be null");
        Assertions.assertEquals(TEST_VALID_EMAIL, actual.getUsername(),
                String.format("Expected email: %s, in UserDetails, but was: %s",
                        TEST_VALID_EMAIL, actual.getUsername()));
        Assertions.assertEquals(TEST_PASSWORD, actual.getPassword(),
                String.format("Expected password: %s, in UserDetails, but was: %s",
                        TEST_PASSWORD, actual.getPassword()));
    }

    @Test
    void loadUserByUsername_UsernameNotFound_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(TEST_INVALID_EMAIL);
        });
    }
}
