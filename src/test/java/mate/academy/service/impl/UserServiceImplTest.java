package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void saveUserViaService_Ok() {
        String email = "Max@i.ua";
        String password = "1234";
        String securedPassword = passwordEncoder.encode(password);
        User userWithSecuredPassword = new User();
        userWithSecuredPassword.setEmail(email);
        userWithSecuredPassword.setPassword(securedPassword);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userDao.save(any())).thenReturn(userWithSecuredPassword);
        User actual = userService.save(user);
        boolean matches = passwordEncoder.matches(password, actual.getPassword());
        Assertions.assertTrue(matches);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userWithSecuredPassword, actual);
    }

    @Test
    void findUserByValidIdViaService_Ok() {
        User user = new User();
        Long id = 1L;
        String email = "Max@i.ua";
        String password = "1234";
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(id);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findUserByNotExistedIdViaService_Null_notOk() {
        Long notExistedId = 2L;
        Mockito.when(userDao.findById(notExistedId)).thenReturn(Optional.ofNullable(null));
        Optional<User> actual = userService.findById(notExistedId);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findUserByValidEmailViaService_Ok() {
        User user = new User();
        Long id = 1L;
        String email = "Max@i.ua";
        String password = "1234";
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(email);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findUserByInvalidEmailViaService_Null_notOk() {
        String notExistedEmail = "Max@i.ua";
        Mockito.when(userDao.findByEmail(notExistedEmail)).thenReturn(Optional.ofNullable(null));
        Optional<User> actual = userService.findByEmail(notExistedEmail);
        Assertions.assertTrue(actual.isEmpty());
    }
}
