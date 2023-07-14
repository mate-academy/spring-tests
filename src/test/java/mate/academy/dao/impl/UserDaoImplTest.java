package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);;
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_invalidUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findById_invalidId_ok() {
        Optional<User> userById = userDao.findById(2L);
        assertTrue(userById.isEmpty());
        assertEquals(Optional.empty(), userById);
    }

    @Test
    void findById_ok() {
        User savedUser = userDao.save(user);
        Optional<User> actual = userDao.findById(savedUser.getId());
        assertTrue(actual.isPresent());
        assertEquals(savedUser.getId(), actual.get().getId());
        assertEquals(savedUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        Optional<User> actual = userDao.findByEmail("");
        assertTrue(actual.isEmpty());
        assertEquals(Optional.empty(), actual);
    }
}
