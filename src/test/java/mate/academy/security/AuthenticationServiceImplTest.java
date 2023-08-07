package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void register_Ok() {
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        User user = new User();
        user.setEmail("id@hello.ua");
        user.setPassword("1234");
        when(userService.save(user)).thenReturn(user);
        when(roleService.getRoleByName("USER")).thenReturn(roleUser);
        String email = "id@hello.ua";
        String password = "1234";
        User registered = authenticationService.register(email, password);
        Assertions.assertNotNull(registered);
        Assertions.assertEquals(email, registered.getEmail());
    }

    @Test
    void register_Invalid_Register_Data_Not_Ok() {
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        when(roleService.getRoleByName("USER")).thenReturn(roleUser);
        String email = "";
        String password = "1234";
        User badUser = new User();
        badUser.setEmail(email);
        badUser.setPassword(password);
        when(userService.save(any())).thenThrow(DataProcessingException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> authenticationService.register(email, password));

    }

    @Test
    void login_Ok() {
        User user = new User();
        user.setEmail("id@hello.ua");
        user.setPassword("1234");
        when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        when(!passwordEncoder.matches(any(), any())).thenReturn(true);
        try {
            Assertions.assertNotNull(authenticationService.login("id@hello.ua", "1234"));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void login_Passwords_Doesnt_Match_Not_Ok() {
        User user = new User();
        user.setEmail("id@hello.ua");
        user.setPassword("1234");
        when(userService.findByEmail(any())).thenReturn(Optional.of(user));
        when(!passwordEncoder.matches(any(), any())).thenReturn(false);
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("id@hello.ua", "1234"));
    }
}
