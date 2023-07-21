package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
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

class UserServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private static final String WRONG_EMAIL = "abc@i.ua";
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private RoleDao roleDao;
    private User bob;
    private Role role;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        roleDao = Mockito.mock(RoleDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        role = new Role(Role.RoleName.USER);
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(passwordEncoder.matches(PASSWORD, actual.getPassword()));
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(bob.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.of(bob));
        Optional<User> actual = userDao.findById(bob.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_NotOk() {
        Mockito.when(userDao.findById(bob.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(bob.getId()).get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        Mockito.when(userDao.findByEmail(WRONG_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userDao.findByEmail(WRONG_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(WRONG_EMAIL).get());
    }
}
