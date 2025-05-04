package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private UserDao userDao;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_validUser_Ok() {
        when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void findUserById_validUser_Ok() {
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        assertFalse(actual.isEmpty());
        assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findUserById_invalidId_NotOk() {
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(999L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findUserByEmail_Ok() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertFalse(actual.isEmpty());
        assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findUserByEmail_invalidEmail_notOk() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail("wjbawkjdbawhb");
        assertTrue(actual.isEmpty());
    }
}
