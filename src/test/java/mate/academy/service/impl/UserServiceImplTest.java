package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User bob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bob = new User();
        bob.setEmail(EMAIL);
        bob.setId(1L);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void findById_ok() {
        when(userDao.findById(1L)).thenReturn(Optional.ofNullable(bob));

        Optional<User> actual = userService.findById(1L);

        assertNotNull(actual.get());
        assertEquals(bob, actual.get());
    }

    @Test
    void findById_nonExistentId_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(bob));

        Optional<User> actual = userService.findById(2L);

        try {
            actual.get();
        } catch (NoSuchElementException e) {
            return;
        }
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(bob.getId()).get());
    }

    @Test
    void findByEmail_ok() {
        when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(bob));

        Optional<User> actual = userService.findByEmail(EMAIL);

        assertNotNull(actual.get());
        assertEquals(bob, actual.get());
    }

    @Test
    void findByEmail_nonExistentEmail_notOk() {
        Mockito.when(userDao.findByEmail("bob@i.ua")).thenReturn(Optional.ofNullable(bob));

        Optional<User> actual = userService.findByEmail("alice@gmail.com");
        try {
            actual.get();
        } catch (NoSuchElementException e) {
            return;
        }
        Assertions.fail("Excepted to receive NoSuchElementException");
    }

    @Test
    void save_ok() {
        when(userDao.save(bob)).thenReturn(bob);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encodedPassword");

        User actual = userService.save(bob);

        assertNotNull(actual);
        assertEquals(bob.getEmail(), actual.getEmail());
        assertEquals(bob.getRoles(), actual.getRoles());
        assertEquals("encodedPassword", actual.getPassword());
    }
}
