package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserUtilForTest userUtil;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        userUtil = new UserUtilForTest();
        expected = new User();
        expected.setEmail(userUtil.getBorisEmail());
        expected.setPassword(userUtil.getBorisPassword());
        expected.setRoles(Set.of(userUtil.getAdminRole()));
    }

    @Test
    void save_validUser_ok() {
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        Mockito.when(passwordEncoder.encode(Mockito.any()))
                .thenReturn(userUtil.getBorisPassword());
        User actual = userService.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void findById_ok() {
        expected.setId(1L);
        Mockito.when(userDao.findById(expected.getId())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
    }

    @Test
    void findById_notValidId_notOk() {
        Mockito.when(userDao.findById(4L)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(4L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_validEmail_ok() {
        Mockito.when(userDao.findByEmail(expected.getEmail())).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail(expected.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_notValidEmail_notOk() {
        Mockito.when(userDao.findByEmail(expected.getEmail())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(expected.getEmail());
        Assertions.assertTrue(actual.isEmpty());
    }
}
