package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.service.UserService;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private User user;

    @BeforeAll
    static  void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @BeforeEach
    void setUp() {
        user = createRawUser();
        user.setId(1L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void saveUser_Ok() {
        User testedUser = createRawUser();
        Mockito.when(userDao.save(testedUser)).thenReturn(user);
        User actual = userService.save(testedUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void saveUser_emptyUser_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> userService.save(null));
    }

    @Test
    void findById_Ok() {
        Optional<User> expected = Optional.of(user);
        Mockito.when(userDao.findById(1L)).thenReturn(expected);
        Optional<User> actual = userService.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_notExistingId_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> expected = Optional.of(user);
        Mockito.when(userDao.findByEmail("user1@gmail.com")).thenReturn(expected);
        Optional<User> actual = userService.findByEmail("user1@gmail.com");
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_notExistingEmail_notOk() {
        Mockito.when(userDao.findByEmail("user1@gmail.com")).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail("user1@gmail.com");
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createRawUser() {
        User user = new User();
        user.setEmail("user1@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}
