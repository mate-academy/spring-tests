package mate.academy.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {
    private static UserDetailsService userDetailsService;

    @BeforeAll
    static void beforeAll() {
        UserService userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        Mockito.when(userService.findByEmail(UserTestUtil.EMAIL))
                .thenReturn(Optional.of(UserTestUtil.getUserBob()));
    }


    @Test
    void loadUserByUsername_Ok() {
        User expected = UserTestUtil.getUserBob();
        UserDetails actual = userDetailsService
                .loadUserByUsername(UserTestUtil.EMAIL);
        checkUser(expected, actual);
    }

    @Test
    void loadUserByUsername_NotOk() {
        User expected = UserTestUtil.getUserBob();
        try {
            UserDetails actual = userDetailsService
                    .loadUserByUsername("incorrect");
            checkUser(expected, actual);
            Assertions.fail("Exception should be thrown "
                    + "while load user with non exist email");
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

}