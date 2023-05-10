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

class UserServiceImplTest {
    private static final Long ID = 1L;
    private static final Long WRONG_ID = 2L;
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "11111111";
    private static final String HASHED_PASSWORD = "oh_here_we_go_again";
    private static final String WRONG_EMAIL = "alice@i,ua";
    private UserService userService;
    private UserDao userDao;
    private User user;
    private Role role;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        userService = new UserServiceImpl(userDao, passwordEncoder);

        role = new Role(ID, Role.RoleName.USER);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(HASHED_PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role)));
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(HASHED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        User actual = userService.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(HASHED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userService.findByEmail(WRONG_EMAIL).get();
        }, "NoSuchElementException expected");
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        User actual = userService.findById(ID).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(HASHED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(Set.of(role), actual.getRoles());
    }

    @Test
    void findById_NotOk() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(new User(ID, EMAIL,
                HASHED_PASSWORD, Set.of(role))));
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userService.findById(WRONG_ID).get();
        }, "NoSuchElementException expected");
    }
}
