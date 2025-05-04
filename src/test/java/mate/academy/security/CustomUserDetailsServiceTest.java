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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "user@com.ua";
    private static final String PASSWORD = "password";
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        UserService userService = Mockito.mock(UserService.class);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(createUser()));
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername_ok() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(EMAIL, userDetails.getUsername());
        Assertions.assertEquals(PASSWORD, userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_incorrectUser_notOk() {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("wrongUser@mail.com"));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
