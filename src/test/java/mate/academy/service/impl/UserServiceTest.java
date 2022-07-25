package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static User user;
    private static User admin;
    private final UserDao userDao = Mockito.mock(UserDao.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserService userService = new UserServiceImpl(userDao, passwordEncoder);

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("user_password");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        user.setId(1L);
        admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword("admin_password");
        admin.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        admin.setId(2L);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(passwordEncoder.encode(anyString())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(any())).thenAnswer(i -> i.getArgument(0));
        User actualUser = userService.save(user);
        assertNotNull(actualUser);
        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getPassword(), actualUser.getPassword());
        assertEquals(user.getRoles().size(), actualUser.getRoles().size());
        assertTrue(actualUser.getRoles().containsAll(user.getRoles()));
    }

    @Test
    void save_exception_notOk() {
        Mockito.when(userDao.save(any())).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.save(user));
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoles().size(), actual.get().getRoles().size());
        assertTrue(actual.get().getRoles().containsAll(user.getRoles()));

        Mockito.when(userDao.findById(admin.getId())).thenReturn(Optional.of(admin));
        actual = userService.findById(admin.getId());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(admin.getId(), actual.get().getId());
        assertEquals(admin.getEmail(), actual.get().getEmail());
        assertEquals(admin.getPassword(), actual.get().getPassword());
        assertEquals(admin.getRoles().size(), actual.get().getRoles().size());
        assertTrue(actual.get().getRoles().containsAll(admin.getRoles()));
    }

    @Test
    void findById_exception_notOk() {
        Mockito.when(userDao.findById(any())).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.findById(user.getId()));
    }
    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoles().size(), actual.get().getRoles().size());
        assertTrue(actual.get().getRoles().containsAll(user.getRoles()));

        Mockito.when(userDao.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        actual = userService.findByEmail(admin.getEmail());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(admin.getId(), actual.get().getId());
        assertEquals(admin.getEmail(), actual.get().getEmail());
        assertEquals(admin.getPassword(), actual.get().getPassword());
        assertEquals(admin.getRoles().size(), actual.get().getRoles().size());
        assertTrue(actual.get().getRoles().containsAll(admin.getRoles()));
    }

    @Test
    void findByEmail_exception_notOk() {
        Mockito.when(userDao.findByEmail(any())).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.findByEmail(user.getEmail()));
    }
}