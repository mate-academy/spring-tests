package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {
    private static final String TEST_EMAIL = "bchupika@mate.academy";
    private static final String TEST_PASSWORD = "12345678";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);

    private final UserService userService = mock(UserService.class);
    private final RoleService roleService = mock(RoleService.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl(
            userService, roleService, passwordEncoder
    );

    static {
        USER_ROLE.setId(1L);
    }

    @Test
    void register_validInput_ok() {
        when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);

        User userToSave = new User();
        userToSave.setEmail(TEST_EMAIL);
        userToSave.setPassword(TEST_PASSWORD);
        userToSave.setRoles(Set.of(USER_ROLE));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(TEST_EMAIL);
        savedUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        savedUser.setRoles(Set.of(USER_ROLE));
        when(userService.save(userToSave)).thenReturn(savedUser);

        User actual = authenticationService.register(TEST_EMAIL, TEST_PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedUser, actual);
    }

    @Test
    void register_emailIsNull_notOk() {
        when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);

        User userToSave = new User();
        userToSave.setEmail(null);
        userToSave.setPassword(TEST_PASSWORD);
        userToSave.setRoles(Set.of(USER_ROLE));

        when(userService.save(userToSave)).thenThrow(DataProcessingException.class);
        assertThrows(
                DataProcessingException.class,
                () -> authenticationService.register(null, TEST_PASSWORD)
        );
    }

    @Test
    void register_passwordIsNull_notOk() {
        when(roleService.getRoleByName("USER")).thenReturn(USER_ROLE);

        User userToSave = new User();
        userToSave.setEmail(TEST_EMAIL);
        userToSave.setPassword(null);
        userToSave.setRoles(Set.of(USER_ROLE));

        when(userService.save(userToSave)).thenThrow(NullPointerException.class);
        assertThrows(
                NullPointerException.class,
                () -> authenticationService.register(TEST_EMAIL, null)
        );
    }

    @Test
    void login_validInputAndUserFound_ok() {
        User found = new User();
        found.setId(1L);
        found.setEmail(TEST_EMAIL);
        found.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        found.setRoles(Set.of(USER_ROLE));
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(found));
    }

    @Test
    void login_validInputAndUserNotFound_notOk() {
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        assertThrows(
                AuthenticationException.class,
                () -> authenticationService.login(TEST_EMAIL, TEST_PASSWORD)
        );
    }

    @Test
    void login_validInputAndPasswordsDoNotMatch_notOk() {
        User found = new User();
        found.setId(1L);
        found.setEmail(TEST_EMAIL);
        found.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        found.setRoles(Set.of(USER_ROLE));
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(found));
        assertThrows(
                AuthenticationException.class,
                () -> authenticationService.login(TEST_EMAIL, "another")
        );
    }
}
