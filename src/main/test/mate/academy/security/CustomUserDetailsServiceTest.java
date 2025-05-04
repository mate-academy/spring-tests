package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_validUsername_Ok() {
        String email = "email@email.ok";
        String password = "12345678";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        when(userService.findByEmail(email)).thenReturn(Optional.of(expectedUser));

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
        when(userService.findByEmail(notExistingEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(notExistingEmail));

        assertEquals("User not found.", exception.getMessage());
    }
}
