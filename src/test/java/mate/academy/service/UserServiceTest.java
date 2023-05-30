package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;
    private static UserDao userDao;
    private static User expectedUser = new User();

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        expectedUser.setEmail("bob@gmail.com");
        expectedUser.setPassword("123456789");
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_Ok() {
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(passwordEncoder.encode(expectedUser.getPassword()))
                .thenReturn(expectedUser.getPassword());
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        User userFomDb = userService.save(expectedUser);
        Assertions.assertNotNull(userFomDb);
        Assertions.assertEquals(expectedUser, userFomDb);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(expectedUser));
        Optional<User> userFomDb = userService.findById(1L);
        Assertions.assertTrue(userFomDb.isPresent());
        Assertions.assertEquals(expectedUser, userFomDb.get());
    }

    @Test
    void findById_NotOk() {
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findById(2L).get());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Optional<User> userFomDb = userService.findByEmail(expectedUser.getEmail());
        Assertions.assertTrue(userFomDb.isPresent());
        Assertions.assertEquals(expectedUser, userFomDb.get());
    }

    @Test
    void findByEmail_NotOk() {
        String notExistUserEmail = "alice@gmail.com";
        Mockito.when(userDao.findByEmail(notExistUserEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail(notExistUserEmail).get());
    }
}
