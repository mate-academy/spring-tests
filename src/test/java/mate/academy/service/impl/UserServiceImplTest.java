package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private static User testUser;

    @BeforeAll
    static void setUpBeforeClass() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        testUser.setId(VALID_ID);
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(VALID_PASSWORD);
        Mockito.when(userDao.save(testUser)).thenReturn(testUser);
        User actual = userService.save(testUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_PASSWORD, actual.getPassword());
        Assertions.assertEquals(testUser, actual);
    }

    @Test
    void findById_existId_ok() {
        Mockito.when(userDao.findById(VALID_ID)).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findById(VALID_ID);
        Assertions.assertEquals(Optional.of(testUser), actual);
    }

    @Test
    void findById_notExistId_emptyOptional() {
        Mockito.when(userDao.findById(INVALID_ID)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(INVALID_ID);
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findByEmail_existEmail_ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        Optional<User> actual = userService.findByEmail(VALID_EMAIL);
        Assertions.assertEquals(Optional.of(testUser), actual);
    }

    @Test
    void findByEmail_notExistEmail_emptyOptional() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
