package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String BOB_EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private static final String ALICE_EMAIL = "alice@gmail.com";
    private static final String ENCODED_PASSWORD = "111111111";
    private static final long BOB_ID = 1L;
    private static final long ALICE_ID = 2L;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User(BOB_ID, BOB_EMAIL, PASSWORD, ROLES);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        Mockito.when(userDao.save(user)).thenReturn(
                new User(BOB_ID, BOB_EMAIL, ENCODED_PASSWORD, ROLES));

        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_ID, actual.getId());
        Assertions.assertEquals(BOB_EMAIL, actual.getEmail());
        Assertions.assertEquals(ENCODED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(BOB_ID)).thenReturn(Optional.of(user));

        User actual = userService.findById(BOB_ID).orElseThrow();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_ID, actual.getId());
        Assertions.assertEquals(BOB_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(BOB_ID)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findById(ALICE_ID);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(BOB_EMAIL)).thenReturn(Optional.of(user));

        User actual = userService.findByEmail(BOB_EMAIL).orElseThrow();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_ID, actual.getId());
        Assertions.assertEquals(BOB_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(ROLES, actual.getRoles());
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail(BOB_EMAIL)).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(ALICE_EMAIL);
        Assertions.assertFalse(actual.isPresent());
    }
}
