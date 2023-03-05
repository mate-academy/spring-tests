package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
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
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "bob@email.com";
    private static final String PASSWORD = "12345678";
    private static final Set<Role> USER_ROLES = Set.of(new Role(Role.RoleName.USER));
    private User user;
    private User savedUser;
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User(EMAIL, PASSWORD, USER_ROLES);
        savedUser = new User(EMAIL, PASSWORD, USER_ROLES);
        savedUser.setId(USER_ID);
    }

    @Test
    void save_correctData_Ok() {
        Mockito.when(userDao.save(any())).thenReturn(savedUser);
        User actualUser = userService.save(user);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(savedUser, actualUser);
    }

    @Test
    void save_NotUniqueEmail_DataProcessingException() {
        Mockito.when(userDao.save(any())).thenThrow(DataProcessingException.class);
        Assertions.assertThrows(DataProcessingException.class, () -> {
            userService.save(user);
        });
    }

    @Test
    void findById_correctData_Ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(savedUser));
        Optional<User> actual = userService.findById(USER_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedUser.getId(), actual.get().getId());
        Assertions.assertEquals(savedUser.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(savedUser.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(savedUser.getRoles(), actual.get().getRoles());
    }

    @Test
    void findById_NotExistId_NotOk() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(USER_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_correctEmail_Ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(savedUser));
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedUser.getId(), actual.get().getId());
        Assertions.assertEquals(savedUser.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(savedUser.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(savedUser.getRoles(), actual.get().getRoles());
    }

    @Test
    void findByEmail_NotExistEmail_NotOk() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }
}
