package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserDaoTest extends AbstractTest {
    private static final String VALID_EMAIL = "bob";
    private static final String VALID_PASSWORD = "1234";
    private static final String NOT_VALID_EMAIL = "taras";
    private UserDao userDao;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob = new User();
        bob.setEmail(VALID_EMAIL);
        bob.setPassword(VALID_PASSWORD);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userDao.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_notOk() {
        userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(bob));
        Optional<User> actual = userDao.findByEmail(NOT_VALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertNotEquals(Optional.of(bob), actual);
    }

    @Test
    void findById_Ok() {
        userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(bob));
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            userDao.findById(1L).get();
        });
    }
}
