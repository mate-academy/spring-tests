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
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);

        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getId(), 1L);
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);

        Mockito.when(userDao.findByEmail(USER_LOGIN)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(USER_LOGIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getEmail(), user.getEmail());
    }
}
