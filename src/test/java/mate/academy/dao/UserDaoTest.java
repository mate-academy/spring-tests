package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12344321";
    private User bob;
    private Role role;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(role));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_NotOk() {
        User alice = new User();
        try {
            userDao.save(alice);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + alice, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        User actual = userDao.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findById_NotOk() {
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("alice@i.ua");
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
