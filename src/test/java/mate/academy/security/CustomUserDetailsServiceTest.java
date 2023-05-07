package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static User defaultUser;
    private static Role defaultRole;
    private CustomUserDetailsService detailsService;
    private UserService userService;

    @BeforeAll
    static void init() {
        defaultRole = new Role();
        defaultRole.setId(1L);
        defaultRole.setRoleName(Role.RoleName.USER);
        defaultUser = new User();
        defaultUser.setId(1L);
        defaultUser.setEmail("default@gmail.com");
        defaultUser.setPassword("12345");
        defaultUser.setRoles(Set.of(defaultRole));
    }

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        detailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUserName_validCase_ok() {
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser));
        UserDetails result = detailsService.loadUserByUsername(defaultUser.getEmail());
        Assertions.assertEquals(
                defaultUser.getEmail(), result.getUsername(),
                "The user's email address must be found");
        Assertions.assertEquals(result.getAuthorities().size(), defaultUser.getRoles().size());
    }

    @Test
    void loadUserByUserName_nullEmail_notOk() {
        Mockito.when(userService.findByEmail(
                defaultUser.getEmail()))
                .thenReturn(Optional.ofNullable(defaultUser));
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername(null),
                "User with a null email must throw exception");
    }

    @Test
    void loadUserByUserName_incorrectEmail_notOk() {
        Mockito.when(userService.findByEmail(
                defaultUser.getEmail())).thenReturn(Optional.ofNullable(defaultUser));
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername("invalid@gmail.com"),
                "User with a incorrect email must throw exception");
    }
}
