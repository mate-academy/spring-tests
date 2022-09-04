package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.RoleName.ADMIN));
        roles.add(new Role(Role.RoleName.USER));
        user.setRoles(new HashSet<>(roles));
    }

    @Test
    void loadUserByUsername_OK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getUsername());
        assertEquals("1234", actual.getPassword());
    }

    @Test
    void loadUserByUsername_emptyUser_notOK() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        try {
            UserDetails actual = userDetailsService.loadUserByUsername(user.getEmail());
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}
