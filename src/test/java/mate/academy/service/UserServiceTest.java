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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String CORRECT_EMAIL = "bob@i.ua";
    private static final String FAKE_EMAIL = "fake@i.ua";
    private static final String PASSWORD = "1234";
    private UserService userService;
    @Mock
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(CORRECT_EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_userExists_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_userByIdNotExists_NotOk() {
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_userExists_Ok() {
        Mockito.when(userDao.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(CORRECT_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_userByEmailNotExists_NotOk() {
        Mockito.when(userDao.findByEmail(FAKE_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(FAKE_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }
}
