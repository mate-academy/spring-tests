package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private User bob;
    private UserDao userDao;

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
        bob.setRoles(Set.of());
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_notOk() {
        userDao.save(bob);
        User repeatBob = bob;
        try {
           userDao.save(repeatBob);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + bob, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById() {
        String email = "bob@i.ua";
        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(email, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }
}