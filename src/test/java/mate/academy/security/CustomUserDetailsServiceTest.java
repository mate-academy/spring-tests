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

public class CustomUserDetailsServiceTest {
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static final String TEST_PASSWORD = "12345";
    private User testUser;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
    }

    @Test
    void loadUserByUsername_LoadWithValidEmailAndPassword_Ok() {
        userDetails = userDetailsService.loadUserByUsername(TEST_EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(TEST_EMAIL,userDetails.getUsername(),
                "The email or password of actual user doesn't match the expected data");
        Assertions.assertEquals(TEST_PASSWORD,userDetails.getPassword(),
                "The email or password of actual user doesn't match the expected data");
    }

    @Test
    void loadUserByUsername_LoadWithIncorrectUsername_NotOk() {
        try {
            userDetailsService.loadUserByUsername("wrong");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals(e.getMessage(),"User not found.");
            return;
        }
        Assertions.fail("UsernameNotFoundException expected");
    }

    @Test
    void loadUserByUsername_LoadWithEmptyUsername_NotOk() {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                        userDetailsService.loadUserByUsername(""),
                "UsernameNotFoundException expected");
    }
}
