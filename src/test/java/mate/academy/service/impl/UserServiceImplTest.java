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
    private static final long USER_ID = 1L;
    private static final String USER_EMAIL = "john@me.com";
    private static final String USER_PASSWORD = "12345678";
    private static final long NON_EXIST_ID = 7L;
    private static final String NON_EXIST_EMAIL = "mary@me.com";
    private User user;
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
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
    void findById_existId_Ok() {
        long existId = user.getId();
        Mockito.when(userDao.findById(existId)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(existId);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles(), actual.get().getRoles());
    }

    @Test
    void findById_nonExistId_notOk() {
        Optional<User> actual = userService.findById(NON_EXIST_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_existEmail_Ok() {
        String existEmail = user.getEmail();
        Mockito.when(userDao.findByEmail(existEmail)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(existEmail);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles(), actual.get().getRoles());
    }

    @Test
    void findByEmail_nonExistEmail_notOk() {
        Optional<User> actual = userService.findByEmail(NON_EXIST_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }
}
