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
    private static final String EMAIl = "bob@mail.com";
    private static final String PASSWORD = "bobNumber1";
    private UserService userService;
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIl);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIl, actual.get().getEmail());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIl)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(EMAIl);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIl, actual.get().getEmail());
    }
}
