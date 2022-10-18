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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String USER_EMAIL = "bob123@gmail.com";
    private static final String USER_PASSWORD = "bob123";
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expected = new User();
        expected.setEmail(USER_EMAIL);
        expected.setPassword(USER_PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        expected.setPassword(passwordEncoder.encode(USER_PASSWORD));
        Mockito.when(userDao.save(expected)).thenReturn(expected);
        User actual = userService.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.get().getRoles());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.findByEmail(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.get().getRoles());
    }
}
