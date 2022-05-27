package mate.academy.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.any;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);

        testUser = new User();
        testUser.setEmail("testUser@mail.com");
        testUser.setPassword("testUser_1234");
        testUser.setRoles(Set.of((new Role(Role.RoleName.ADMIN))));
    }

    @Test
    void loadUserByUsername_existUser_Ok() {
        String existUserEmail = testUser.getEmail();
        Mockito.when(userService.findByEmail(existUserEmail)).thenReturn(Optional.of(testUser));
        UserDetails actual = customUserDetailsService.loadUserByUsername(existUserEmail);
        Assertions.assertEquals(testUser.getEmail(), actual.getUsername());
        Assertions.assertEquals(testUser.getPassword(), actual.getPassword());
        Assertions.assertEquals(testUser.getRoles().size(), actual.getAuthorities().size(),
                "Number of roles is not equal");
        List<String> actualRoles = actual.getAuthorities().stream()
                .map(r -> new StringBuilder().append(r).substring(5))
                .collect(Collectors.toList());
        List<String> expectedRoles = testUser.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
        Assertions.assertTrue(expectedRoles.containsAll(actualRoles), "Different set of role.");
    }

    @Test
    void loadUserByUsername_nonExistUser_NotOk() {
        String nonExistUserEmail = "non_Exist_User_Email@mail.com";
        Mockito.when(userService.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistUserEmail),
                "Expected UsernameNotFoundException "
                        + "while trying to get non-existent user by email.\n");
    }
}