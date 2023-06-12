package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "qwerty";
    private static final String NOT_EXISTING_EMAIL = "alice@i.ua";
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserDao userDao;
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDaoImpl.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void save_validUserWithEncodedPassword_Ok() {
        //arrange
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn(encodedPassword);
        Mockito.when(userDao.save(user)).thenReturn(user);

        //act
        User actual = userService.save(user);

        //assert
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(encodedPassword, actual.getPassword());
        Mockito.verify(userDao).save(user);
        Mockito.verify(passwordEncoder).encode(PASSWORD);
    }

    @Test
    void findById_userExist_Ok() {
        //arrange
        Long existId = 1L;
        Mockito.when(userDao.findById(existId)).thenReturn(Optional.of(user));

        //act
        Optional<User> actual = userService.findById(existId);

        //assert
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Mockito.verify(userDao).findById(existId);
    }

    @Test
    void findById_userByNotExistId_NotOk() {
        //arrange
        Long notExistId = 2L;
        Mockito.when(userDao.findById(notExistId)).thenReturn(Optional.empty());

        //act
        Optional<User> actual = userService.findById(notExistId);

        //assert
        Assertions.assertTrue(actual.isEmpty());
        Mockito.verify(userDao).findById(notExistId);
    }

    @Test
    void findById_userByNullId_NotOk() {
        Mockito.when(userDao.findById(null)).thenReturn(Optional.empty());

        //act
        Optional<User> actual = userService.findById(null);

        //assert
        Assertions.assertTrue(actual.isEmpty());
        Mockito.verify(userDao).findById(null);
    }

    @Test
    void findByEmail_userExist_Ok() {
        //arrange
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        //act
        Optional<User> actual = userService.findByEmail(EMAIL);

        //assert
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Mockito.verify(userDao).findByEmail(actual.get().getEmail());
    }

    @Test
    void findByEmail_userByNotExistEmail_NotOk() {
        //arrange
        Mockito.when(userDao.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        //act
        Optional<User> actual = userService.findByEmail(NOT_EXISTING_EMAIL);

        //assert
        Assertions.assertTrue(actual.isEmpty());
        Mockito.verify(userDao).findByEmail(NOT_EXISTING_EMAIL);
    }

    @Test
    void findByEmail_userByNullEmail_NotOk() {
        //arrange
        Mockito.when(userDao.findByEmail(null)).thenReturn(Optional.empty());

        //act
        Optional<User> actual = userService.findByEmail(null);

        //assert
        Assertions.assertTrue(actual.isEmpty());
        Mockito.verify(userDao).findByEmail(null);
    }
}
