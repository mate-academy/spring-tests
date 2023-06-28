package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static final String VALID_EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String INVALID_EMAIL = "alice@gmail.com";
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserService userService;
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, encoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(PASSWORD);
        userRole = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void save_ok() {
        Mockito.when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertTrue(actual.getRoles().contains(userRole));
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(user.getEmail());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void findById_userAbsent_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(1L);
        Assertions.assertFalse(userOptional.isPresent());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(userOptional.isPresent());
    }
}
