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
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void saveUser_Ok() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userDao.save(user))
                .thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findUserById_Ok() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(id);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(id, actual.get().getId());
    }

    @Test
    void findUserById_invalidId_NotOk() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findUserByEmail_Ok() {
        String email = "bob@i.ua";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(email);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(email, actual.get().getEmail());
    }

    @Test
    void findUserByEmail_invalidEmail_notOk() {
        String email = "bob@i.ua";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail("alice@i.ua");
        Assertions.assertTrue(actual.isEmpty());
    }
}
