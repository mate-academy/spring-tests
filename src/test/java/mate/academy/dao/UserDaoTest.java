package mate.academy.dao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTest extends AbstractTest {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        User actual = userDao.save(bob);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        userDao.save(bob);
        User actual = userDao.findById(bob.getId()).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        userDao.save(bob);
        User actual = userDao.findByEmail("bob@i.ua").get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
    }


    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
