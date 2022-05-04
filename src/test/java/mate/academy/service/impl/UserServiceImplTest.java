package mate.academy.service.impl;

import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao,
                new BCryptPasswordEncoder());
    }

    @Test
    void save() {
        User expected = UserTestUtil.getUserBob();
        Mockito.when(userDao.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User argument = invocation.getArgument(0);
                    argument.setId(1L);
                    return argument;
                });
        User actual = userService.save(expected);
        Assertions.assertEquals(1L, actual.getId());
        checkUser(expected, actual);
    }


    @Test
    void findById_Ok() {
        User expected = UserTestUtil.getUserBob();
        Mockito.when(userDao.findById(1L))
                .thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        checkUser(expected, actual.get());
    }

    @Test
    void findById_NotOk() {
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User expected = UserTestUtil.getUserBob();
        Mockito.when(userDao.findByEmail(UserTestUtil.EMAIL))
                .thenReturn(Optional.of(expected));
        Optional<User> actual = userService
                .findByEmail(UserTestUtil.EMAIL);
        Assertions.assertTrue(actual.isPresent());
        checkUser(expected, actual.get());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userService.findByEmail(UserTestUtil.EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    private void checkUser(User expected, User actual) {
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(UserTestUtil.getListOfStringRoles(expected),
                UserTestUtil.getListOfStringRoles(actual));
    }
}