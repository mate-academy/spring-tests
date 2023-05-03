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
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }
    
    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(passwordEncoder.matches("1234", actual.getPassword()));
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> optional = userService.findById(1L);
        Assertions.assertTrue(optional.isPresent());
        User actual = optional.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        Optional<User> optional = userService.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(optional.isPresent());
        User actual = optional.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_IncorrectEmail_NotOk() {
        Optional<User> actual = userService.findByEmail("IncorrectEmail");
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
