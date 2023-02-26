package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User bob;

    @BeforeEach
    void setUp() {
        bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userDao = Mockito.mock(UserDaoImpl.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(bob));
        Mockito.when(userDao.findByEmail("bob@i.ua")).thenReturn(Optional.ofNullable(bob));
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual);
        Assertions.assertTrue(passwordEncoder.matches("1234", actual.getPassword()));
    }

    @Test
    void findById_ExistentId_IsOk() {
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(bob, actual.get());
    }

    @Test
    void findById_NotExistentId_NotOk() {
        try {
            userService.findById(2L).get();
        } catch (NoSuchElementException ex) {
            Assertions.assertEquals(ex.getLocalizedMessage(), "No value present");
            return;
        }
        Assertions.fail("Excepted to receive NoSuchElementException");
    }

    @Test
    void findByEmail_correctEmail_isOk() {
        Mockito.when(userDao.findByEmail("bob@i.ua")).thenReturn(Optional.ofNullable(bob));
        Optional<User> actual = userService.findByEmail("bob@i.ua");
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(bob, actual.get());
    }

    @Test
    void findByEmail_NotExistentEmail_throwException() {
        try {
            userService.findByEmail("alice@i.ua").get();
        } catch (NoSuchElementException ex) {
            Assertions.assertEquals(ex.getLocalizedMessage(), "No value present");
            return;
        }
        Assertions.fail("Excepted to receive NoSuchElementException");
    }
}
