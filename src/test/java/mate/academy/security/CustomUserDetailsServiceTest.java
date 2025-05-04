package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    @Mock
    private UserService userService;
    private UserDetailsService userDetailsService;
    private Role user;
    private Role admin;
    private User bob;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
        user = new Role(Role.RoleName.USER);
        admin = new Role(Role.RoleName.ADMIN);
        bob = new User(EMAIL, PASSWORD);
    }

    @Test
    void loadUserByUsername_validEmail_Ok() {
        bob.setRoles(Set.of(user));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        String expected = "ROLE_" + user.getRoleName().name();
        String actualRole = actual.getAuthorities().toArray()[0].toString();
        assertEquals(expected, actualRole);
    }

    @Test
    void loadUserByUsername_twoRoles_Ok() {
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(admin);
        roles.add(user);
        bob.setRoles(roles);
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        String[] expected = roles.stream()
                .map(x -> "ROLE_" + x.getRoleName().name())
                .toArray(String[]::new);
        assertEquals(expected[0], actual.getAuthorities().toArray()[0].toString());
        assertEquals(expected[1], actual.getAuthorities().toArray()[1].toString());
    }

    @Test
    void loadUserByUsername_wrongEmail_notOk() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL));
    }
}
