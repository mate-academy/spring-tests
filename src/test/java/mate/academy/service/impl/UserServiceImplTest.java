package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
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
    private static final String USERNAME = "bob@i.ua";
    private static final String PASSWORD = "12345678";
    private static final Long ID = 1L;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;
    private User expectedUser;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userDao = Mockito.mock(UserDaoImpl.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        roleUser = new Role(Role.RoleName.USER);
        user = new User(USERNAME, PASSWORD, Set.of(roleUser));
        expectedUser = new User(USERNAME, PASSWORD, Set.of(roleUser));
        expectedUser.setId(ID);
    }

    @Test
    void save_ok() {
        Mockito.when(userDao.save(user)).thenReturn(expectedUser);
        User actualUser = userService.save(user);
        Assertions.assertNotNull(actualUser);
        assertUsers(expectedUser, actualUser);
    }

    @Test
    void save_notUniq_notOk() {
        Mockito.when(userDao.save(user)).thenThrow(DataProcessingException.class);
        DataProcessingException thrown =
                Assertions.assertThrows(DataProcessingException.class,
                        () -> userService.save(user),
                        "Expected to receive DataProcessingException");
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(expectedUser));
        Optional<User> actualOptionalUser = userService.findById(ID);
        Assertions.assertTrue(actualOptionalUser.isPresent());
        assertUsers(expectedUser, actualOptionalUser.get());
    }

    @Test
    void findById_notFound_notOk() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.empty());
        Optional<User> actualOptionalUser = userService.findById(ID);
        Assertions.assertTrue(actualOptionalUser.isEmpty());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(USERNAME)).thenReturn(Optional.of(expectedUser));
        Optional<User> actualOptionalUser = userService.findByEmail(USERNAME);
        Assertions.assertTrue(actualOptionalUser.isPresent());
        assertUsers(expectedUser, actualOptionalUser.get());
    }

    void assertUsers(User expectedUser, User actualUser) {
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    }
}
