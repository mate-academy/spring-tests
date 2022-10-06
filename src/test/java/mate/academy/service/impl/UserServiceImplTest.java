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
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String USER_EMAIL = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void save_ValidUser_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_ValidId_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        User actual = userService.findById(user.getId()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail() {
        Mockito.when(userDao.findByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(USER_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }
}
