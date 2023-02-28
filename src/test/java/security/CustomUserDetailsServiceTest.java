package security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest {
    private static UserService userService;
    private static UserDetailsService detailsService;
    private static UserDetails expectedUserDetails;
    private static final String USER_EMAIL = "alice@mail.com";
    private User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        detailsService = new CustomUserDetailsService(userService);
    }

    @BeforeEach
    void setUp() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.RoleName.USER.name()));
        expectedUserDetails = new org.springframework.security.core.userdetails.User(USER_EMAIL,
                "password", authorities);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword("password");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        UserDetails actualUserDetails = detailsService.loadUserByUsername(USER_EMAIL);
        assertEquals(expectedUserDetails.getUsername(), actualUserDetails.getUsername());
        assertEquals(expectedUserDetails.getPassword(), actualUserDetails.getPassword());
        assertEquals(expectedUserDetails.getAuthorities(), actualUserDetails.getAuthorities());
    }

    @Test
    void loadUserByUsername_emailNotExists_NotOk() {
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                detailsService.loadUserByUsername(USER_EMAIL),
                "Method should throw UsernameNotFoundException if user was not found");
    }

    @Test
    void loadUserByUsername_userIsNull_NotOk() {
        assertThrows(UsernameNotFoundException.class, () ->
                        detailsService.loadUserByUsername(null),
                "Method should throw UsernameNotFoundException if user is null");
    }
}
