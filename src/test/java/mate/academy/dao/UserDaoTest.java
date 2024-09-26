package mate.academy.dao;

import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest{
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        user = new User();
        user.setEmail("bob@i.com");
        user.setPassword("1234");
        userDao.save(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, user.getId());
    }

    @Test
    void findByEmail() {
        user = new User();
        user.setEmail("bob@i.com");
        user.setPassword("1234");
        userDao.save(user);
        Assertions.assertNotNull(user);
        Optional<User> byEmail = userDao.findByEmail("bob@i.com");
        Assertions.assertEquals(user.getId(), byEmail.orElseThrow().getId());
    }

    @Test
    void findById() {
        user = new User();
        user.setEmail("bob@i.com");
        user.setPassword("1234");
        User savedUser = userDao.save(user);
        Optional<User> byEmail = userDao.findById(1L);
        Assertions.assertEquals(savedUser.getId(), byEmail.orElseThrow().getId());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}