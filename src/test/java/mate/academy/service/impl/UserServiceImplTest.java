package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        String email = "email@com.ua";
        String password = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        user.setRoles(Set.of(role));
        user.setId(1L);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userDao.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userDao.findById(-1L)).thenThrow(DataProcessingException.class);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(userDao.findByEmail(Mockito.any())).thenReturn(Optional.empty());
    }

    @Test
    public void save_ok() {
        String email = "email@com.ua";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        user.setRoles(Set.of(role));
        user.setId(1L);
        User actual = userService.save(user);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getRoles().stream().map(e ->
                        e.getRoleName().name()).findFirst().get(),
                actual.getRoles().stream().map(e -> e.getRoleName().name()).findFirst().get());
    }

    @Test
    public void save_null_notOk() {
        Assertions.assertThrows(NullPointerException.class, () -> userService.save(null));
    }

    @Test
    public void findById_ok() {
        Assertions.assertTrue(userService.findById(1L).isPresent());
        User actualUser = userService.findById(1L).get();
        Assertions.assertEquals("email@com.ua", actualUser.getEmail());
        Assertions.assertEquals("encodedPassword", actualUser.getPassword());
        Assertions.assertEquals(1L, actualUser.getId());
        Assertions.assertEquals("USER",
                actualUser.getRoles().stream().map(e -> e.getRoleName().name()).findFirst().get());
    }

    @Test
    public void findById_negativeId_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userService.findById(-1L));
    }

    @Test
    public void findByEmail_ok() {
        Assertions.assertTrue(userService.findById(1L).isPresent());
        User actualUser = userService.findById(1L).get();
        Assertions.assertEquals("email@com.ua", actualUser.getEmail());
        Assertions.assertEquals("encodedPassword", actualUser.getPassword());
        Assertions.assertEquals(1L, actualUser.getId());
        Assertions.assertEquals("USER",
                actualUser.getRoles().stream().map(e -> e.getRoleName().name()).findFirst().get());
    }

    @Test
    public void findByEmail_notExitEmail_notOk() {
        Assertions.assertTrue(userService.findByEmail("wrong@df.dc").isEmpty());
    }
}
