package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class CustomUserDetailsServiceTest {
    private UserDetailsService userDetailsService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User expected = UserTestUtil.getUserBob();
        prepareMock(expected);
        UserDetails actual = userDetailsService
                .loadUserByUsername(UserTestUtil.EMAIL);
        checkUser(expected, actual);
    }

    @Test
    void loadUserByUsername_NotOk() {
        User expected = UserTestUtil.getUserBob();
        prepareMock(expected);
        try {
            UserDetails actual = userDetailsService
                    .loadUserByUsername("incorrect");
            checkUser(expected, actual);
            Assertions.fail("Exception should be thrown");
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.",
                    e.getMessage());
        }
    }

    private void checkUser(User expected, UserDetails actual) {
        Assertions.assertEquals(expected.getEmail(), actual.getUsername());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        List<String> expectedRoles = UserTestUtil.getListOfStringRoles(expected);
        List<String> actualRoles = actual.getAuthorities().stream()
                .map(a -> a.getAuthority().split("_")[1])
                .collect(Collectors.toList());
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    private void prepareMock(User expected) {
        Mockito.when(userService.findByEmail(UserTestUtil.EMAIL))
                .thenReturn(Optional.of(expected));
    }
}