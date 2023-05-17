package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123";
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class,Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);

        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        userDao.save(bob);

        User actual = userDao.findById(1L).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(),bob.getEmail());
    }

    @Test
    void findById_Not_Existing_Id() {

        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        userDao.save(bob);

        Optional<User> actualOptional = userDao.findById(2L);

        Assertions.assertTrue(actualOptional.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        bob.setRoles(Set.of(role));
        userDao.save(bob);

        User actual = userDao.findByEmail(bob.getEmail()).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(),bob.getEmail());
    }

    @Test
    void findByEmail_Not_Existing_Email() { //not existing
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        bob.setRoles(Set.of(role));
        userDao.save(bob);

        Optional<User> actualOptional = userDao.findByEmail("ivan@gmail.com");

        Assertions.assertTrue(actualOptional.isEmpty());
    }
}
