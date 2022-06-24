package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private String email;
    private String password;
    private Set<Role> roles;
    private String incorrectEmail;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        email = "bchupika@mate.academy";
        password = "12345678";
        roles = Set.of(new Role(Role.RoleName.USER));
        incorrectEmail = "asfdasfasff@gmail.com";
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setId(1L);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual, "UserDetails must not be null for email: " + email);
        Assertions.assertEquals(email,actual.getUsername(),"Email excepted: " + email
                + " but was: " + actual.getUsername());
    }

    @Test
    void loadUserByUsername_NotOk() {
        Mockito.when(userService.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {customUserDetailsService.loadUserByUsername(incorrectEmail);},
                "UsernameNotFoundException is expected with email: " + incorrectEmail);
    }
}