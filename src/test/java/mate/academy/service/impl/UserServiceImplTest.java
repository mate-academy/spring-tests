package mate.academy.service.impl;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;
    private static UserDao userDao;
    private static String email = "max@i.ua";
    private static String password = "12345";
    private User user;
    private User actual;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.ofNullable(user));
        actual = userService.save(user);
    }

    @Test
    void saveUser_Ok() {
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByExistId_Ok() {
        Optional<User> actualOptional = userService.findById(1L);
        assertTrue(actualOptional.isPresent());
        assertEquals(user.getEmail(), actualOptional.get().getEmail());
        assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findByNotExistId_NotOk() {
        try {
            userService.findById(100L).get();
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getLocalizedMessage());
            return;
        }
        fail("Excepted to receive NoSuchElementException");
    }

    @Test
    void findByExistEmail_Ok() {
        Optional<User> actualOptional = userService.findByEmail(email);
        user.setId(1L);
        assertTrue(actualOptional.isPresent());
        assertEquals(1L, actualOptional.get().getId());
        assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findByNotExistEmail_NotOk() {
        try {
            userService.findByEmail("wrond@i.ua").get();
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getLocalizedMessage());
            return;
        }
        fail("Excepted to receive NoSuchElementException");
    }
}
