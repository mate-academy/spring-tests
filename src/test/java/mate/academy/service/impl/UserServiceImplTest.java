package mate.academy.service.impl;

import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;

class UserServiceImplTest {
    private static final String LOGIN = "hello@i.am";
    private static final String PASSWORD = "1221";
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User hello;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        hello = new User();
        hello.setEmail(LOGIN);
        hello.setPassword(PASSWORD);
        hello.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(hello)).thenReturn(hello);
        User actual = userService.save(hello);
        Assertions.assertNotNull(actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(hello));
        Optional<User> actual = userService.findById(1L);
        Assertions.assertNotNull(actual);
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(hello.getEmail())).thenReturn(Optional.of(hello));
        Optional<User> actual = userService.findByEmail(hello.getEmail());
        Assertions.assertNotNull(actual);
    }
}
