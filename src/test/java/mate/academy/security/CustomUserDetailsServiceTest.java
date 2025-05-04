package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static CustomUserDetailsService customUserDetailsService;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    public void loadUserByUsername_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        User bob = new User();
        bob.setId(2L);
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(userRole));
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(bob));

        UserDetails actual = customUserDetailsService.loadUserByUsername("bob@i.ua");
        Assertions.assertEquals("bob@i.ua", actual.getUsername());
        Assertions.assertEquals("1234", actual.getPassword());
        Assertions.assertEquals(1, actual.getAuthorities().size());
        Assertions.assertEquals("ROLE_USER", actual.getAuthorities().stream()
                .findFirst().get().getAuthority());
    }

    @Test
    public void loadUserByUsername_notExistingUser_notOk() {
        Mockito.when(userService.findByEmail("nosuchemail@i.ua")).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nosuchemail@i.ua"));
    }

    @Test
    public void loadUserByUsername_nullInput_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }
}
