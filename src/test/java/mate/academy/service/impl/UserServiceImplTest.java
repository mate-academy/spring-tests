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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao,passwordEncoder);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user,actual);
    }

    @Test
    void findById_Ok() {
        Long id = user.getId();
        userService.save(user);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findById(id);
        Assertions.assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(id,actual.getId());
        Assertions.assertEquals(user.getEmail(),actual.getEmail());
        Assertions.assertEquals(user.getPassword(),actual.getPassword());
    }

    @Test
    void findById_Not_Ok() {
        Long id = user.getId();
        userService.save(user);
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findById(3L);
        Assertions.assertFalse(actualOptional.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        String email = user.getEmail();
        userService.save(user);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findByEmail(email);
        Assertions.assertTrue(actualOptional.isPresent());
        User actual = actualOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email,actual.getEmail());
        Assertions.assertEquals(user.getEmail(),actual.getEmail());
        Assertions.assertEquals(user.getPassword(),actual.getPassword());
    }

    @Test
    void findByEmail_Not_Ok() {
        Optional<User> actual = userService.findByEmail("alice.ua");
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
