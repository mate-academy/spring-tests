package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractDaoTest {
    private static final String INCORRECT_EMAIL = "alice@i.ua";
    private UserDao userDao;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob = new User();
        bob.setEmail("bob2@i.ua");
        bob.setPassword("1234");
        Optional<User> existingUser = userDao.findByEmail(bob.getEmail());
        if (existingUser.isEmpty()) {
            userDao.save(bob);
        }
    }

    @Test
    void save_ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");

        User actual = userDao.save(bob);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertTrue(actual.getId() > 0L);
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        Optional<User> actual = userDao.findByEmail(bob.getEmail());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_NonExistentEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(INCORRECT_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(bob.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_NotExistentId_notOk() {
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }
}
