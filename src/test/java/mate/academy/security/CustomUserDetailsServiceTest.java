package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private UserUtilForTest userUtil;

    @BeforeEach
    void setUp() {
        userUtil = new UserUtilForTest();
        userService = Mockito.mock(UserServiceImpl.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        User expected = userUtil.getUserBoris();
        expected.setRoles(Set.of(userUtil.getAdminRole()));
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.of(expected));

        UserDetails actual = customUserDetailsService.loadUserByUsername(expected.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getUsername());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals("[ROLE_ADMIN]", actual.getAuthorities().toString());
    }

    @Test
    void loadUserByUsername_usernameNotFoundException_notOk() {
        Mockito.when(userService.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        try {
            customUserDetailsService.loadUserByUsername("randomUser");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to get UsernameNotFoundException "
                + "while trying to get not existent user");
    }
}
