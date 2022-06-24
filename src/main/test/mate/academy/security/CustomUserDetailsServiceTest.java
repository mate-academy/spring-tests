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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_validUsername_Ok() {
        String email = "email@email.ok";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        UserDetails actualUserDetails = customUserDetailsService.loadUserByUsername(email);
        assertNotNull(actualUserDetails);
        assertEquals(email, actualUserDetails.getUsername());
        assertEquals(password, actualUserDetails.getPassword());
        assertEquals(1, actualUserDetails.getAuthorities().size());
    }

    @Test
    void loadUserByUsername_validUsername_UserNotFound() {
        String notExistingEmail = "notexisting@email.ok";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(notExistingEmail);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(notExistingEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NumberFormatException.class, () -> {
            customUserDetailsService.loadUserByUsername(notExistingEmail);
        });
        assertEquals("User not found.", exception.getMessage());
    }
}