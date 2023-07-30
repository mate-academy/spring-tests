package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static final String TEST_PASSWORD = "12345";
    private User testUser;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
    }

    @Test
    void saveUser_UserSaved_Ok() {
        Mockito.when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        Mockito.when(userDao.save(testUser)).thenReturn(testUser);
        User user = userService.save(testUser);
        Assertions.assertEquals(user.getEmail(),TEST_EMAIL,
                "The email of actual user doesn't match the expected email");
    }

    @Test
    void findUserByEmail_UserFound_Ok() {
        testUser.setId(1L);
        Mockito.when(userDao.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        User actualUser = userService.findByEmail(TEST_EMAIL).get();
        Assertions.assertEquals(actualUser.getId(),1L,
                "The id of actual user doesn't match the expected id");
    }

    @Test
    void findUserByEmail_FindUserByNonExistingEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        userService.findByEmail("wrong").get(),
                "NoSuchElementException for empty email expected");
    }

    @Test
    void findUserByEmail_FindUserByEmptyEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        userService.findByEmail("").get(),
                "NoSuchElementException for empty email expected");
    }

    @Test
    void findUserById_UserFound_Ok() {
        testUser.setId(1L);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(testUser));
        User actualUser = userService.findById(1L).get();
        Assertions.assertEquals(actualUser.getEmail(),TEST_EMAIL,
                "The email of actual user doesn't match the expected email");
    }

    @Test
    void findUserById_findUserByNonExistingId_NotOk() {
        Mockito.when(userService.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        userService.findById(2L).get(),
                "NoSuchElementException for non-existing id expected");
    }

    @Test
    void findUserById_findUserByNullId_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        userService.findById(null).get(),
                "NoSuchElementException for null id expected");
    }
}
