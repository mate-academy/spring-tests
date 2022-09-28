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

public class UserDaoTest extends AbstractTest {
    public static final String EMAIL = "bob@i.ua";
    public static final String PASSWORD = "1234";
    private UserDao userDao;
    private RoleDao roleDao;

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
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        userDao.save(bob);
        User actual = userDao.findById(bob.getId()).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findById_NoUserInDb_Ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(role);
        bob.setRoles(Set.of(role));
        User save = userDao.save(bob);
        User actual = userDao.findByEmail(EMAIL).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findByEmail_NoEmailInDb_Ok() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
