package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private User admin;
    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword("super1234");
        admin.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }
    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(admin.getEmail())).thenReturn(Optional.ofNullable(admin));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(admin.getEmail());
        assertEquals(admin.getEmail(), userDetails.getUsername());
        assertEquals(admin.getPassword(), userDetails.getPassword());
    }
    @Test
    void loadUserByUsername_not_Ok(){
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(admin.getEmail()));
    }
}
