package mate.academy.service;

import java.util.NoSuchElementException;
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

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;
    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        String email = "alice@com.ua";
        user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode("1234")).thenReturn("1234");
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail("alice@com.ua")).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findByEmail("alice@com.ua");
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_emailNotFound_notOk() throws NoSuchElementException {
        Mockito.when(userDao.findByEmail("bob@com.ua")).thenReturn(Optional.empty());
        userService.findByEmail("bob@com.ua");
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById(1L);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_idNotFound_notOk() throws NoSuchElementException {
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.empty());
        userService.findById(2L);
    }
}
