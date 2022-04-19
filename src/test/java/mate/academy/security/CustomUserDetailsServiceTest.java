package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "hello@i.am";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String PASSWORD = "1221";
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;
    private User hello;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        hello = new User();
        hello.setEmail(EMAIL);
        hello.setPassword(PASSWORD);
        hello.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(hello));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(hello.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(hello.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(ROLE_USER, userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining()));
    }

    @Test
    void loadUserByUsername_notOk() {
        String notExist = "bodika@you.are";
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(hello));
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(notExist));
    }
}
