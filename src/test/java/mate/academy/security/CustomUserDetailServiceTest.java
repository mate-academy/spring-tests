package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailServiceTest {
    private static final String LOGIN = "bob";
    private CustomUserDetailsService customUserDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        UserService userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(LOGIN)).thenReturn(Optional.ofNullable(user));
    }

    @Test
    void loadByUsername_Ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        user.setEmail(LOGIN);
        user.setPassword("1234");

        UserDetails details = customUserDetailsService.loadUserByUsername(LOGIN);

        Assertions.assertNotNull(details);
        Assertions.assertEquals(user.getEmail(), details.getUsername());
        Assertions.assertEquals(user.getPassword(), details.getPassword());
    }

    @Test
    void loadByUsername_NotOk() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService
                .loadUserByUsername("wrongUsername"));
    }
}
