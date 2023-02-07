package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("12345");

        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        User actualUser = actual.get();
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(bob.getEmail(), actualUser.getEmail());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@i.ua");
        userDao.save(bob);

        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(bob);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(bob.getId(), actual.get().getId());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
