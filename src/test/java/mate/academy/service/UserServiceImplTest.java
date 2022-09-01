package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        User expected = new User();
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        User actual = userService.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void save_emptyUser_notOk() {
        User user = new User();
        Mockito.when(userDao.save(user)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.save(user),
                "Should receive DataProcessingException");
    }

    @Test
    void findById_ok() {
        User expected = new User();
        expected.setId(2L);
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        Mockito.when(userDao.findById(expected.getId())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_noUserById_notOk() {
        Mockito.when(userDao.findById(1L)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.findById(1L),
                "Should receive DataProcessingException");
    }

    @Test
    void findById_idIsNull_notOk() {
        Mockito.when(userDao.findById(null)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.findById(null),
                "Should receive DataProcessingException");
    }

    @Test
    void findByEmail_ok() {
        User expected = new User();
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        Mockito.when(userDao.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail(expected.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_noUserBySuchEmail_notOk() {
        Mockito.when(userDao.findByEmail("harrington@.ua"))
                .thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class,
                () -> userService.findByEmail("harrington@.ua"),
                "Should receive DataProcessingException");
    }
}
