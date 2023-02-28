package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@e.mail";
    private static final Long ID = 1L;
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User(EMAIL, "qwerty", Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(user, actual);
    }

    @Test
    void save_notUniqueEmail_notOk() {
        userDao.save(user);
        Assertions.assertEquals("Can't create entity: " + user,
                Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user))
                        .getMessage());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_notFound_notOk() {
        Assertions.assertTrue(userDao.findByEmail(EMAIL).isEmpty());
    }

    @Test
    void findById_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ID, actual.get().getId());
    }

    @Test
    void findById_notFound_notOk() {
        Assertions.assertTrue(userDao.findById(ID).isEmpty());
    }
}
