package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class AuthenticationServiceImplTest {
    private final static String EMAIL = "bob@gmail.com";
    private final static String PASSWORD = "12345678";
    private AuthenticationService authenticationService;
    private UserService userService;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
        userService = Mockito.mock(UserService.class);
        authenticationService = new AuthenticationServiceImpl(
                userService,
                roleService,
                new BCryptPasswordEncoder()
        );
    }

    @Test
    void register_Ok() {
        Role userRole = new Role(Role.RoleName.USER);
        Mockito.when(roleService.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(userRole);
        Mockito.when(userService.save(Mockito.argThat(
                u -> u != null
                        && u.getPassword().equals(PASSWORD)
                        && u.getEmail().equals(EMAIL)
                        && u.getRoles().contains(userRole))))
                .thenAnswer((Answer<User>) invocation -> {
                    User user1 = invocation.getArgument(0);
                    user1.setId(1L);
                    return user1;
                });
        User actual = authenticationService.register(EMAIL, PASSWORD);
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void login_Ok() {
        User expected = getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login(EMAIL, PASSWORD);
            checkUsers(expected, actual);
        } catch (AuthenticationException e) {
            Assertions.fail("No exception should be thrown: " + e.getMessage());
        }
    }

    @Test
    void loginWithIncorrectEmail_NotOk() {
        User expected = getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login("incorrect", PASSWORD);
            checkUsers(expected, actual);
            Assertions.fail("Exception should be thrown");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",
                    e.getMessage());
        }
    }

    @Test
    void loginWithIncorrectPassword_NotOk() {
        User expected = getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login(EMAIL, "incorrect");
            checkUsers(expected, actual);
            Assertions.fail("Exception should be thrown");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",
                    e.getMessage());
        }
    }

    private void checkUsers(User expected, User actual) {
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        List<String> actualRoles = getListOfStringRoles(actual);
        List<String> expectedRoles = getListOfStringRoles(expected);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    private void prepareMockForLogin(User expected) {
        expected.setPassword(new BCryptPasswordEncoder().encode(expected.getPassword()));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(expected));
    }

    private List<String> getListOfStringRoles(User user) {
        return user.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
    }

    private User getUserBob() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role userRole = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(userRole));
        return user;
    }
}