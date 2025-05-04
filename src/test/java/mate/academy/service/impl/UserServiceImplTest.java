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
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail("bob@gmail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        User actual = userService.findById(user.getId()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_InvalidId_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(2L).get(), "Expected to recieve NoSuchElementException");
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(user.getEmail()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findByEmail_InvalidEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("qwerty@gmail.com").get(),
                "Expected to recieve NoSuchElementException");
    }
}
