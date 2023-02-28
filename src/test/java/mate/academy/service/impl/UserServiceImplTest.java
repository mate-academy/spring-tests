package mate.academy.service.impl;

import java.util.NoSuchElementException;
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
    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_EMAIL = "mark@i.ua";
    private static final String DEFAULT_PASSWORD = "12345678";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(DEFAULT_ID);
        user.setEmail(DEFAULT_EMAIL);
        user.setPassword(DEFAULT_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void save_ok() {
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(user.getPassword()))
                .thenReturn(encodedPassword);
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(encodedPassword, actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_ok() {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userDao.findById(user.getId())).thenReturn(optionalUser);
        Optional<User> actual = userService.findById(user.getId());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles(), actual.get().getRoles());
    }

    @Test
    void findById_nonExistingId_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findById(2L).get());
    }

    @Test
    void findByEmail_ok() {
        Optional<User> optionalUser = Optional.of(user);
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(optionalUser);
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles(), actual.get().getRoles());
    }

    @Test
    void findByEmail_nonExistingEmail_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("non-existing@i.com").get());
    }
}
