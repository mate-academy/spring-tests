package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final Role.RoleName USER = Role.RoleName.USER;
    private UserService underTest;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        passwordEncoder = mock(PasswordEncoder.class);
        underTest = new UserServiceImpl(userDao, passwordEncoder);
        role = new Role(USER);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void saveSuccess() {
        when(userDao.save(user)).thenReturn(user);
        User actual = underTest.save(user);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void findByIdSuccess() {
        when(userDao.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = underTest.findById(USER_ID);
        assertNotNull(actual.get());
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmailSuccess() {
        when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = underTest.findByEmail(USER_EMAIL);
        assertNotNull(actual.get());
        assertEquals(user, actual.get());
    }
}
