package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserDaoImplTest extends AbstractTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "12345678";
    private UserDao userDao;
    private PasswordEncoder encoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, encoder);
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        Mockito.when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setId(1L);
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findById(user.getId());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setId(1L);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> userOptional = userService.findByEmail(user.getEmail());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void findById_idDoesNotExist_notOk() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setId(1L);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findById(1L);
        Assertions.assertFalse(userOptional.isPresent());
    }

    @Test
    void findByEmail_emailDoesNotExist_notOk() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(VALID_EMAIL);
        Assertions.assertFalse(userOptional.isPresent());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}
