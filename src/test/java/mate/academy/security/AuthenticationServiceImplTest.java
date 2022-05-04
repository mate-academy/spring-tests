package mate.academy.security;

import java.util.List;
import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthenticationServiceImplTest {
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
                        && u.getPassword().equals(UserTestUtil.PASSWORD)
                        && u.getEmail().equals(UserTestUtil.EMAIL)
                        && u.getRoles().contains(userRole))))
                .thenAnswer((Answer<User>) invocation -> {
                    User user1 = invocation.getArgument(0);
                    user1.setId(1L);
                    return user1;
                });
        User actual = authenticationService.register(UserTestUtil.EMAIL,
                UserTestUtil.PASSWORD);
        Assertions.assertEquals(UserTestUtil.EMAIL, actual.getEmail());
    }

    @Test
    void login_Ok() {
        User expected = UserTestUtil.getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login(UserTestUtil.EMAIL,
                    UserTestUtil.PASSWORD);
            checkUsers(expected, actual);
        } catch (AuthenticationException e) {
            Assertions.fail("No exception should be thrown: " + e.getMessage());
        }
    }

    @Test
    void loginWithIncorrectEmail_NotOk() {
        User expected = UserTestUtil.getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login("incorrect",
                    UserTestUtil.PASSWORD);
            checkUsers(expected, actual);
            Assertions.fail("Exception should be thrown");
        } catch (AuthenticationException e) {
            Assertions.assertEquals("Incorrect username or password!!!",
                    e.getMessage());
        }
    }

    @Test
    void loginWithIncorrectPassword_NotOk() {
        User expected = UserTestUtil.getUserBob();
        prepareMockForLogin(expected);
        try {
            User actual = authenticationService.login(UserTestUtil.EMAIL,
                    "incorrect");
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
        List<String> actualRoles = UserTestUtil.getListOfStringRoles(actual);
        List<String> expectedRoles = UserTestUtil.getListOfStringRoles(expected);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

    private void prepareMockForLogin(User expected) {
        expected.setPassword(new BCryptPasswordEncoder().encode(expected.getPassword()));
        Mockito.when(userService.findByEmail(UserTestUtil.EMAIL))
                .thenReturn(Optional.of(expected));
    }

}