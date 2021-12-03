package mate.academy.dao;

import java.util.Collections;
import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends AbstractTest{
    private static UserDao userDao;
    private static User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("123");
        user.setRoles(Collections.emptySet());
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L,actual.getId());
        assertEquals(user,actual);
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        assertFalse(actual.isEmpty());
        assertEquals(1L,actual.get().getId());
        assertEquals(user,actual.get());
        Optional<User> emptyValue = userDao.findByEmail("alice@i.ua");
        assertTrue(emptyValue.isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        User bob = new User();
        bob.setEmail("bobby@i.ua");
        bob.setPassword("222");
        bob.setRoles(Collections.emptySet());
        userDao.save(bob);
        Optional<User> actual = userDao.findById(2L);
        assertFalse(actual.isEmpty());
        assertEquals(2L,actual.get().getId());
        assertEquals(bob,actual.get());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}