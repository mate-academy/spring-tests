package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail("bob@gmail.com");
        user.setPassword("12345678");
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void findById_userByIdExists_Ok() {
        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123456789");
        User alice = new User();
        alice.setEmail("alice@gmail.com");
        alice.setPassword("qwertyqw");
        userDao.save(bob);
        userDao.save(alice);
        Optional<User> actualOptional = userDao.findById(2L);
        assertTrue(actualOptional.isPresent());
        assertEquals(alice.getEmail(), actualOptional.get().getEmail());
    }

    @Test
    void findById_userByIdNotExist_Ok() {
        Optional<User> actualOptional = userDao.findById(3L);
        assertTrue(actualOptional.isEmpty());
    }

    @Test
    void findByEmail_userByEmailExists_Ok() {
        User bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setPassword("123456789");
        User expected = userDao.save(bob);
        Optional<User> actualOptional = userDao.findByEmail("bob@gmail.com");
        assertTrue(actualOptional.isPresent());
        assertEquals(expected.getId(), actualOptional.get().getId());
    }

    @Test
    void findByEmail_userByEmailNotExist_Ok() {
        Optional<User> actualOptional = userDao.findByEmail("testuser@gmail.com");
        assertTrue(actualOptional.isEmpty());
    }
}
