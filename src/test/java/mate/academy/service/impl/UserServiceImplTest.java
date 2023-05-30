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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, encoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(encoder.matches("1234", actual.getPassword()));
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        User actual = userOptional.get();
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_idDoesNotExist_notOk() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertFalse(userOptional.isPresent());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(user.getEmail());
        User actual = userOptional.get();
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void findByEmail_emailDoesNotExist_notOk() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(VALID_EMAIL);
        Assertions.assertFalse(userOptional.isPresent());
    }
}
