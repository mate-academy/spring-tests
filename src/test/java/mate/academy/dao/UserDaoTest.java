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
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validUser_Ok() {
        User actual = createUserAndSave();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_validEmail_Ok() {
        createUserAndSave();
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("wdadawdawvefe");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_validId_Ok() {
        createUserAndSave();
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_invalidId_notOk() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    private User createUserAndSave() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user.setRoles(Set.of(role));
        return userDao.save(user);
    }
}
