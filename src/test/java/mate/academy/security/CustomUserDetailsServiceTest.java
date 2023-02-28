package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.Set;

class CustomUserDetailsServiceTest {
    private static CustomUserDetailsService customUserDetailsService;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserServiceImpl.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
        String correctEmail = "bchupika@mate.academy";
        String password = "12345678";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Long identifier = 1L;
        user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        user.setId(identifier);
    }

    @Test
    void loadUserByUsername_Ok() {
        String email = "bchupika@mate.academy";
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails actual = customUserDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual, "UserDetails must not be null for email: " + email);
        Assertions.assertEquals(email,actual.getUsername(),"Email excepted: " + email
                + " but was: " + actual.getUsername());
    }

    @Test
    void loadUserByUsername_nonExistentEmail_NotOk() {
        String incorrectEmail = "asfdasfasff@gmail.com";
        Mockito.when(userService.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {customUserDetailsService.loadUserByUsername(incorrectEmail);},
                "UsernameNotFoundException is expected with email: " + incorrectEmail);
    }
}
