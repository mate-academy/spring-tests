package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String TEST_EMAIL = "bob@i.ua";
    private static final String TEST_PASSWORD = "password";
    private UserService userService;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(TEST_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void loadUserByUsername_ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(TEST_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_EMAIL, actual.getUsername(),
                String.format("Expected user's name should be %s, but was %s",
                        TEST_EMAIL, actual.getUsername()));
        Assertions.assertEquals("password", actual.getPassword(),
                String.format("Expected password should be %s, but was %s",
                        TEST_PASSWORD, actual.getPassword()));
    }

    @Test
    void loadUserByUsername_UserNameNotFound_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                        userDetailsService.loadUserByUsername("nonExisted@i.ua"),
                "Expected UsernameNotFoundException to be thrown");
    }
}
