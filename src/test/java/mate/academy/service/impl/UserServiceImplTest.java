package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User bob;
    private String email = "bob@i.ua";
    private String password = "1234";

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), bob.getId());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findByEmail(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getPassword(), password);
        Assertions.assertEquals(actual.get().getEmail(), email);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(bob.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getPassword(), password);
        Assertions.assertEquals(actual.get().getId(), bob.getId());
    }

    @Test
    void findByEmail_DataProcessingException_NotOk() {
        Mockito.when(userDao.findByEmail(null))
                .thenThrow(DataProcessingException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userService.findByEmail(null));
    }

    @Test
    void findById_DataProcessingException_NotOk() {
        Mockito.when(userDao.findById(null))
                .thenThrow(DataProcessingException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userService.findById(null));
    }
}
