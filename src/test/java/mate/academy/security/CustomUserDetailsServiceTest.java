package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class CustomUserDetailsServiceTest {
    private static UserService userService;
    private static UserDetailsService userDetailsService;
    private User bob;

    @BeforeAll
     static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @BeforeEach
    void setUp() {
        bob = getTestUser("bob@gmail.com", "1234", Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.of(bob));
        UserDetails actual = userDetailsService.loadUserByUsername(bob.getEmail());
        assertNotNull(actual);
        assertEquals(bob.getEmail(), actual.getUsername());
        assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void loadUserByUsername_nonExistentEmail_notOk() {
        assertThrows(Exception.class, () -> userDetailsService.loadUserByUsername(any()));
    }

    private User getTestUser(String email, String password, Set<Role> roles) {
        mate.academy.model.User user = new mate.academy.model.User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}
