package mate.academy.security;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        userServiceMock = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userServiceMock);
    }

    @Test
    void loadUserByUsername_ExistedUser_Ok() {
        String email = "user@gmail.com";
        String password = "123";
        Set<Role> roles = Set.of(new Role(ADMIN));
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        Mockito.when(userServiceMock.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(email, actual.getUsername());
        assertEquals(password, actual.getPassword());
    }

    @Test
    void loadUserByUsername_NotExistedUser_NotOk() {
        String email = "user@gmail.com";
        Mockito.when(userServiceMock.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }
}
