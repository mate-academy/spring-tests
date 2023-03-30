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
    private static final String TEST_USER_EMAIL = "testuser@i.ua";
    private static final String USER_NOT_FROM_DB_EMAIL = "notformdbuser@i.ua";
    private static final String TEST_USER_PASSWORD = "87654321";
    private static final Role TEST_USER_ROLE = new Role(Role.RoleName.USER);
    private UserDetailsService userDetailsService;
    private UserService userService;
  
    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        User defaultUser = new User();
        defaultUser.setEmail(TEST_USER_EMAIL);
        defaultUser.setPassword(TEST_USER_PASSWORD);
        defaultUser.setRoles(Set.of(new Role(TEST_USER_ROLE.getRoleName())));
    
        Mockito.when(userService.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(defaultUser));
    }
  
    @Test
    void loadUserByUserName_Ok() {
        UserDetails actual = userDetailsService.loadUserByUsername(TEST_USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_USER_EMAIL, actual.getUsername());
        Assertions.assertEquals(TEST_USER_PASSWORD, actual.getPassword());
    }
  
    @Test
    void loadUserByUserName_UserNotFound() {
        try {
            userDetailsService.loadUserByUsername(USER_NOT_FROM_DB_EMAIL);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
