package mate.academy.service;

import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private static final long ID = 1L;
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private User user;
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDao = mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = createUser();
        user.setId(ID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void save_Ok() {
        User newUser = createUser();
        when(userDao.save(newUser))
                .thenReturn(user);
        User actual = userService.save(newUser);
        assertNotNull(actual);
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_Ok() {
        when(userDao.findById(ID))
                .thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(ID);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_NoIdInDB_NotOk() {
        when(userDao.findById(ID))
                .thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(ID);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        when(userDao.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(user.getId(), actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_NoEmailInDB_Ok() {
        when(userDao.findByEmail(EMAIL))
                .thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(EMAIL);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
        return user;
    }
}
