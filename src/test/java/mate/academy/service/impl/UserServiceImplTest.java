package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserService userService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("den@gmail.com");
        user.setPassword("77777777");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actualUser = userService.save(user);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        User actualUser = userService.findById(user.getId()).get();
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void findById_NoSuchElementException() {
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    userService.findById(user.getId()).get();
                }, "NoSuchElementException was expected");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.ofNullable(user));
        User actualUser = userService.findByEmail(user.getEmail()).get();
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    void findByEmail_NoSuchElementException() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    userService.findByEmail(user.getEmail()).get();
                },"NoSuchElementException was expected");
    }
}
