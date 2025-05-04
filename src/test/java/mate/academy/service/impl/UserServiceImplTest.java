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
    private static final String EMAIL = "user@email.com";
    private static final String PASSWORD = "password";
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
    void save_ok() {
        Mockito.when(userDao.save(any())).thenReturn(savedUser);
        User actualUser = userService.save(user);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(savedUser, actualUser);
    }

    @Test
    void save_NotUniqueEmail_Exception() {
        Mockito.when(userDao.save(any())).thenThrow(DataProcessingException.class);
        try {
            userService.save(user);
        } catch (Exception exception) {
            Assertions.assertEquals(DataProcessingException.class, exception.getClass());
            return;
        }
        Assertions.fail("There is DataProcessingException expected!");
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(savedUser));
        Optional<User> actualOptionalUser = userService.findById(USER_ID);
        Assertions.assertNotNull(actualOptionalUser);
        Assertions.assertEquals(savedUser.getId(), actualOptionalUser.get().getId());
        Assertions.assertEquals(savedUser.getEmail(), actualOptionalUser.get().getEmail());
        Assertions.assertEquals(savedUser.getPassword(), actualOptionalUser.get().getPassword());
        Assertions.assertEquals(savedUser.getRoles(), actualOptionalUser.get().getRoles());
    }

    @Test
    void findById_NotExistId_Empty() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        Optional<User> actualOptionalUser = userService.findById(USER_ID);
        Assertions.assertTrue(actualOptionalUser.isEmpty());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(savedUser));
        Optional<User> actualOptionalUser = userService.findByEmail(EMAIL);
        Assertions.assertNotNull(actualOptionalUser);
        Assertions.assertEquals(savedUser.getId(), actualOptionalUser.get().getId());
        Assertions.assertEquals(savedUser.getEmail(), actualOptionalUser.get().getEmail());
        Assertions.assertEquals(savedUser.getPassword(), actualOptionalUser.get().getPassword());
        Assertions.assertEquals(savedUser.getRoles(), actualOptionalUser.get().getRoles());
    }

    @Test
    void findByEmail_NotExistEmail_Empty() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.empty());
        Optional<User> actualOptionalUser = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actualOptionalUser.isEmpty());
    }
}
