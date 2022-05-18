package mate.academy.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    private UserService userService;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        String email = "bchupika@mate.academy";
        String password = "12345678";
        expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void loadUserByUsername_validData_ok() {
        Mockito.when(userService.findByEmail(expectedUser.getEmail())).thenReturn(Optional.of(expectedUser));
        UserDetails actualUser = userDetailsService.loadUserByUsername(expectedUser.getEmail());
        assertNotNull(expectedUser.getEmail());
        assertEquals(expectedUser.getEmail(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }
}
