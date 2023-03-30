package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.UserDao;
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
    private static UserService userService;
    private static final String email = "usermail@gmail.com";
    private User user;

    @BeforeEach
    void setUp() {
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(email);
        String password = "12345678";
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(user)).thenReturn(user);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.ofNullable(user));
    }

    @Test
    void saveUser_ok() {
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findById_idExists_ok() {
        Optional<User> actualOptional = userService.findById(1L);
        Assertions.assertTrue(actualOptional.isPresent());
        Assertions.assertEquals(user.getEmail(), actualOptional.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findById_idDoesntExist_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(100L).get());
    }

    @Test
    void findByEmail_emailExists_ok() {
        Optional<User> actualOptional = userService.findByEmail(email);
        user.setId(1L);
        Assertions.assertTrue(actualOptional.isPresent());
        Assertions.assertEquals(1L, actualOptional.get().getId());
        Assertions.assertEquals(user.getPassword(), actualOptional.get().getPassword());
    }

    @Test
    void findByEmail_incorrectEmail_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("usermail1@gmail.com").get(),
                "Excepted to receive NoSuchElementException.");
    }
}
