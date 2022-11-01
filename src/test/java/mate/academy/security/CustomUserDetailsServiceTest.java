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

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private CustomUserDetailsService customUserDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        UserService userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
    }

    @Test
    void loadUserByUsername_Ok() {
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword("122345678");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        UserDetails details = customUserDetailsService.loadUserByUsername(EMAIL);

        Assertions.assertNotNull(details);
        Assertions.assertEquals(user.getEmail(), details.getUsername());
        Assertions.assertEquals(user.getPassword(), details.getPassword());
    }

    @Test
    void loadUserByUsername_NotOk() {
        try {
            customUserDetailsService.loadUserByUsername("fds@fd.hsdj");
        } catch (Exception e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
