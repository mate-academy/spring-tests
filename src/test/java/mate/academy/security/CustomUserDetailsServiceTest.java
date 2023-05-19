package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;
    private User expected;

    @BeforeEach
    void setUp() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(1L);
        expected = new User();
        expected.setEmail("test@gmail.com");
        expected.setPassword("12345678");
        expected.setRoles(Set.of(userRole));
        expected.setId(1L);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void userDetails_loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(expected));
        UserDetails userDetails = userDetailsService.loadUserByUsername(expected.getEmail());
        Assertions.assertEquals(expected.getEmail(),userDetails.getUsername());
        Assertions.assertEquals(expected.getPassword(),userDetails.getPassword());
        Assertions.assertEquals(1,userDetails.getAuthorities().size());
    }

    @Test
    void userDetails_loadUserByUsernameNull_NotOk() {
        Mockito.when(userService.findByEmail(null)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, ()
                -> userDetailsService.loadUserByUsername(null));
    }
}
