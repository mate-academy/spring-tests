package mate.academy.service;

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
    private static final String EMAIL = "tom@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Long WRONG_ID = 999L;
    private static final String WRONG_EMAIL = "invalid_email";
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private User actualUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        userRole = new Role(Role.RoleName.USER);
        actualUser = getActualUser();
    }

    @Test
    void save_Ok() {
        userDao.save(actualUser);
        Mockito.when(userDao.save(actualUser)).thenReturn(actualUser);
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        User savedUser = userService.save(actualUser);
        Assertions.assertEquals(savedUser.getEmail(), EMAIL);
    }

    @Test
    void findById_Ok() {
        userDao.save(actualUser);
        Mockito.when(userDao.findById(actualUser.getId())).thenReturn(Optional.of(actualUser));
        User userId = userService.findById(actualUser.getId()).orElseThrow();
        Assertions.assertEquals(userId.getId(), actualUser.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(actualUser);
        Mockito.when(userDao.findByEmail(actualUser.getEmail()))
                .thenReturn(Optional.of(actualUser));
        User userEmail = userService.findByEmail(actualUser.getEmail()).orElseThrow();
        Assertions.assertEquals(userEmail.getEmail(), actualUser.getEmail());
    }

    @Test
    void save_NotOk() {
        userDao.save(actualUser);
        Mockito.when(userDao.save(actualUser)).thenReturn(null);
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Assertions.assertNull(userService.save(actualUser));
    }

    @Test
    void findById_NullId_NotOk() {
        userDao.save(actualUser);
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findById(null).isEmpty());
    }

    @Test
    void findById_WrongId_NotOk() {
        userDao.save(actualUser);
        Mockito.when(userDao.findById(WRONG_ID)).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findById(WRONG_ID).isEmpty());
    }

    @Test
    void findByEmail_EmptyEmail_NotOk() {
        actualUser.setEmail("");
        userDao.save(actualUser);
        Mockito.when(userDao.findByEmail("")).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findByEmail("").isEmpty());
    }

    @Test
    void findByEmail_WrongEmail_NotOk() {
        userDao.save(actualUser);
        Mockito.when(userDao.findByEmail(WRONG_EMAIL)).thenReturn(Optional.empty());
        Assertions.assertTrue(userService.findByEmail(WRONG_EMAIL).isEmpty());
    }

    private User getActualUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        return user;
    }
}
