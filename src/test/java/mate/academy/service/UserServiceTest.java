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

class UserServiceTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "123456";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);

        User actual = userService.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Long id = user.getId();
        userService.save(user);

        Mockito.when(userDao.findById(id)).thenReturn(Optional.ofNullable(user));
        Optional<User> byId = userService.findById(id);

        Assertions.assertTrue(byId.isPresent());
        Assertions.assertEquals(Optional.ofNullable(user), byId);
    }

    @Test
    void findById_NotOk() {
        Long id = user.getId();
        userService.save(user);

        Mockito.when(userDao.findById(id))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byId = userService.findById(6L);

        Assertions.assertFalse(byId.isPresent());
        Assertions.assertEquals(Optional.empty(), byId);
    }

    @Test
    void findByEmail_Ok() {
        String email = user.getEmail();
        userService.save(user);

        Mockito.when(userDao.findByEmail(email))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byEmail = userService.findByEmail(email);

        Assertions.assertTrue(byEmail.isPresent());
        Assertions.assertEquals(Optional.ofNullable(user), byEmail);
    }

    @Test
    void findByEmail_NotOk() {
        String email = user.getEmail();
        userService.save(user);

        Mockito.when(userDao.findByEmail(email))
                .thenReturn(Optional.ofNullable(user));
        Optional<User> byEmail = userService.findByEmail("alice");

        Assertions.assertFalse(byEmail.isPresent());
        Assertions.assertEquals(Optional.empty(), byEmail);
    }
}
