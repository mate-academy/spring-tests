package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private final UserDao userDao = mock(UserDao.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserServiceImpl(userDao, passwordEncoder);

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test");
        user.setPassword("123");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_validUser_ok() {
        User saved = new User();
        saved.setId(1L);
        saved.setEmail(user.getEmail());
        saved.setPassword(user.getPassword());
        saved.setRoles(user.getRoles());
        when(userDao.save(user)).thenReturn(saved);

        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(saved, actual);
    }

    @Test
    void save_emailIsNull_notOk() {
        user.setEmail(null);
        when(userDao.save(user)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.save(user));
    }

    @Test
    void save_userIsNull_notOk() {
        assertThrows(NullPointerException.class, () -> userService.save(null));
    }

    @Test
    void findByEmail_userFound_ok() {
        user.setId(1L);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_userNotFound_ok() {
        when(userDao.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        Optional<User> actual = userService.findByEmail("notfound@mail.com");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_emailIsNull_ok() {
        when(userDao.findByEmail(null)).thenReturn(Optional.empty());

        Optional<User> actual = userService.findByEmail(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_userFound_ok() {
        user.setId(1L);
        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findById_userNotFound_ok() {
        when(userDao.findById(2L)).thenReturn(Optional.empty());

        Optional<User> actual = userService.findById(2L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_emailIsNull_ok() {
        when(userDao.findById(null)).thenReturn(Optional.empty());

        Optional<User> actual = userService.findById(null);
        assertTrue(actual.isEmpty());
    }
}
