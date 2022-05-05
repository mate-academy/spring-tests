package mate.academy.dao;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345678";
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
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Collections.emptySet());
    }

    @Test
    void save_notExistingUser_ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_userWithExistingId_notOk() {
        userDao.save(bob);
        bob.setId(1L);
        try {
            userDao.save(bob);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + bob, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_existingId_ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals("bob@i.ua", actual.get().getEmail());
        Assertions.assertEquals("12345678", actual.get().getPassword());
    }

    @Test
    void findById_notExistingId_ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
