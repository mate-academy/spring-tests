package mate.academy.service.impl;

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
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String EMAIL_IS_NOT_IN_DB = "alice@i.ua";
    private static final Long ID = 1L;
    private static final Long ID_IS_NOT_IN_DB = 2L;
    private static final String PASSWORD = "1234";
    private UserService userService;
    private UserDao userDao;
    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_emailIsNotInDb_notOk() {
        Mockito.when(userDao.findByEmail(EMAIL_IS_NOT_IN_DB))
                        .thenReturn(Optional.empty());
        try {
            userService.findByEmail(EMAIL_IS_NOT_IN_DB);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById(ID);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_wrongUserId_notOk() {
        Mockito.when(userDao.findById(ID_IS_NOT_IN_DB))
                .thenReturn(Optional.empty());
        try {
            userService.findById(ID_IS_NOT_IN_DB);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }
}
