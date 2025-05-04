package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "qwerty";
    private static final String NOT_EXISTING_EMAIL = "alice@i.ua";
    private UserService userService;
    private UserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        userDetailsService = new CustomUserDetailsService(userService);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void loadUserByUsername_Ok() {
        //arrange
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        //act
        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

        //assert
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getUsername());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void loadUserByUsername_usernameNotFound_NotOk() {
        //arrange
        Mockito.when(userService.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        //act & assert
        UsernameNotFoundException exception =
                Assertions.assertThrows(UsernameNotFoundException.class,
                        () -> userDetailsService.loadUserByUsername(NOT_EXISTING_EMAIL));
        Assertions.assertEquals("User not found.", exception.getMessage());
        Mockito.verify(userService).findByEmail(NOT_EXISTING_EMAIL);
    }
}
