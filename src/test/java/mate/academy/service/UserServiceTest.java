package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
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
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void saveUser_Ok() {
        String email = "bob@i.ua";
        String password = "1234";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(password);
        Mockito.when(userDao.save(Mockito.any())).thenReturn(user);
        User actual = userService.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void saveUser_DaoSaveException_NotOk() {
        User user = new User();
        Mockito.when(userDao.save(Mockito.any())).thenThrow(
                new DataProcessingException("Can't create entity: " + user, new Exception()));
        try {
            userService.save(user);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail("Incorrect user should throw DataProcessingException");
    }

    @Test
    void findByEmail_Ok() {
        String email = "bob@i.ua";
        String password = "1234";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        User actual = userService.findByEmail(email).orElse(new User());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_ThrowException() {
        Mockito.when(userDao.findByEmail(Mockito.anyString()))
                .thenThrow(new NoSuchElementException("No value present"));
        try {
            userService.findByEmail(Mockito.anyString());
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Unavailable user should throw NoSuchElementException");
    }

    @Test
    void findById_Ok() {
        Long id = 1L;
        String email = "bob@i.ua";
        String password = "1234";

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(user));

        User actual = userService.findById(id).orElse(new User());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_ThrowException() {
        Mockito.when(userDao.findById(Mockito.anyLong()))
                .thenThrow(new NoSuchElementException("No value present"));
        try {
            userService.findById(Mockito.anyLong());
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Unavailable user should throw NoSuchElementException");
    }
}
