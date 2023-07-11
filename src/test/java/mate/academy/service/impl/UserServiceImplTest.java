package mate.academy.service.impl;

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
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_validUser_ok() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findUserById_validUser_ok() {
        User user = createUserAndSave();
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findUserById_invalidId_notOk() {
        User user = createUserAndSave();
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(999L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findUserByEmail_ok() {
        User user = createUserAndSave();
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findUserByEmail_invalidEmail_notOk() {
        User user = createUserAndSave();
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail("wjbawkjdbawhb");
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createUserAndSave() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
