package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        User actual = userDao.save(bob);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail("bob@i.ua");

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_NonExistentEmail_notOk() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail("alice@i.ua");

        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);

        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_NotExistentId_notOk() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        userDao.save(bob);
        Optional<User> actual = userDao.findById(2L);

        Assertions.assertTrue(actual.isEmpty());
    }
}
