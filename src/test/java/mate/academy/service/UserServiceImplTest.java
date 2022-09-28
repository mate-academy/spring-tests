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
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String EMAIL = "modernboy349@gmail.com";
    private static final String PASSWORD = "Hello123";
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        User actual = userService.findById(user.getId()).get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(user.getEmail()).get();
        Assertions.assertEquals(user, actual);
    }
}
