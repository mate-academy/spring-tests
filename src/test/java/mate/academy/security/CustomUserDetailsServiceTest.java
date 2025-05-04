package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;
    private String login;
    private String password;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        login = "vvv@i.ua";
        password = "12341234";
    }

    @Test
    void loadUserByUsername_Ok() {
        User vvv = new User();
        vvv.setEmail(login);
        vvv.setPassword(password);
        vvv.setRoles(Set.of(new Role(RoleName.USER)));
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(vvv));
        UserDetails actual = customUserDetailsService.loadUserByUsername(login);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual.getUsername());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void loadUserByUsername_UsernameNotFound_NotOk() {
        User vvv = new User();
        vvv.setEmail(login);
        vvv.setPassword(password);
        vvv.setRoles(Set.of(new Role(RoleName.USER)));
        Mockito.when(userService.findByEmail(login)).thenReturn(Optional.of(vvv));
        try {
            customUserDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UserNotFoundException");
    }
}
