package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomUserDetailsService detailsService;

    @Test
    void loadUserByUsername_Ok() {
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        User user = new User();
        user.setEmail("id@hello.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(roleUser));
        when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        Assertions.assertNotNull(detailsService.loadUserByUsername("id@hello.ua"));
    }

    @Test
    void loadByUsername_NotOk_Throw_Exception() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername("id@hello.ua"));
    }
}
