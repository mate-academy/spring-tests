package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static String ENCRYPTED_PASSWORD =
            "$2a$12$PTnGH9fcDJ0NHX3MpbWKu.EJokPYBxXIrb9AVOFoY7ThRMUXo76bq";
    private static Long ID = 1L;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User("validEmail@gmail.com", "validPassword");
    }

    @Test
    void save_validUser_ok() {
        User expected = new User(1L, "validEmail@gmail.com", ENCRYPTED_PASSWORD);
        when(userDao.save(any())).thenReturn(expected);
        when(passwordEncoder.encode("validPassword"))
                .thenReturn(ENCRYPTED_PASSWORD);
        User actual = userService.save(user);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void findById_validId_ok() {
        user.setId(ID);
        user.setPassword(ENCRYPTED_PASSWORD);
        when(userDao.findById(ID)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(ID);
        if (actual.isEmpty()) {
            fail();
        }
        assertEquals(ID, actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_invalidId_ok() {
        when(userDao.findById(ID)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(ID);
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_validEmail_ok() {
        user.setId(ID);
        user.setPassword(ENCRYPTED_PASSWORD);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        if (actual.isEmpty()) {
            fail();
        }
        assertEquals(ID, actual.get().getId());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(ENCRYPTED_PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }
}
