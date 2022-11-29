package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private final static String EMAIL = "denis@mail.ru";
    private static final String PASSWORD = "12345678";
    private static final Role USER_ROLE = new Role();
    private UserDetailsService userDetailsService;
    private UserService userService;

    static {
        USER_ROLE.setRoleName(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        User denis = new User();
        denis.setEmail(EMAIL);
        denis.setPassword(PASSWORD);
        denis.setRoles(Set.of(USER_ROLE));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(denis));
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        assertEquals(denis.getEmail() ,userDetails.getUsername());
        assertEquals(denis.getPassword(), userDetails.getPassword());
        assertEquals(denis.getRoles()
                .stream().map(i -> "ROLE_" + i.getRoleName())
                .collect(Collectors.toList()), userDetails.getAuthorities().stream()
                .map(Object::toString).toList());

    }

    @Test
    void loadUserByUsername_notOk() {
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(EMAIL));
    }

}