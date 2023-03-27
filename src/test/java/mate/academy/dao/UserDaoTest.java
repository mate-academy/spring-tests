package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final Long ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;
    private static final String EMAIL = "bob@mate.academy";
    private static final String NOT_EXISTING_EMAIL = "alice@mate.academy";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void save_savedUser_notOk() {
        userDao.save(bob);
        assertThrows(DataProcessingException.class, () -> userDao.save(bob));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getEmail(), EMAIL);
        assertEquals(actual.get().getPassword(), PASSWORD);
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        assertTrue(userDao.findByEmail(NOT_EXISTING_EMAIL).isEmpty());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        assertTrue(userDao.findByEmail(null).isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(ID);
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getEmail(), EMAIL);
        assertEquals(actual.get().getPassword(), PASSWORD);
    }

    @Test
    void findByEmail_notExistingId_notOk() {
        assertTrue(userDao.findById(NOT_EXISTING_ID).isEmpty());
    }

    @Test
    void findByEmail_nullId_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.findById(null));
    }
}
