package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void saveUser_Ok() {
        User user = new User();
        user.setEmail("john@ukr.net");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByExistId_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@ukr.net");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(user));
        Optional<User> actualOptional = userService.findById(1L);
        assertTrue(actualOptional.isPresent());
        assertEquals(user.getEmail(), actualOptional.get().getEmail());
        assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findByNotExistId_NotOk() {
        Mockito.when(userDao.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userService.findById(100L).get();
        }, "Expected to receive NoSuchElementException");
    }

    @Test
    void findByExistEmail_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@ukr.net");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findByEmail("john@ukr.net")).thenReturn(Optional.ofNullable(user));
        Optional<User> actualOptional = userService.findByEmail("john@ukr.net");
        assertTrue(actualOptional.isPresent());
        assertEquals(1L, actualOptional.get().getId());
        assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findByNotExistEmail_NotOk() {
        Mockito.when(userDao.findByEmail("minimal@ukr.net")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userService.findByEmail("minimal@ukr.net").get();
        }, "Expected to receive NoSuchElementException");
    }
}
