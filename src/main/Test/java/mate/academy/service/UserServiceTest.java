package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
    private static final String EMAIL ="bchupika@mate.academy";
    private static final String UNVALID_EMAIL ="bchupikamate.academy";
    private static final String PASSWORD ="1234";
    private static final Long USER_ID = 1L;
    private static final Long UNVALID_USER_ID = 2L;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(anyString())).thenAnswer(i -> i.getArgument(0));
        Mockito.when(userDao.save(any())).thenAnswer(i -> i.getArgument(0));
        User actual = userService.save(user);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(USER_ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findById(UNVALID_USER_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        Optional<User> actual = userService.findByEmail(UNVALID_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }
}
