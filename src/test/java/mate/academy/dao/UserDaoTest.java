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
    private static final String EMAIL = "bob@mail.com";
    private static final String PASSWORD = "1234";
    private static final String WRONG_EMAIL = "wrongEmail@mail.com";
    private UserDao userDao;
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = roleDao.save(new Role(Role.RoleName.USER));
    }

    @Test
    void save_Ok() {
        User bob = createTestUser();
        User actual = userDao.save(bob);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(bob.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_Ok() {
        User testUser = userDao.save(createTestUser());
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(testUser.getId(), actual.get().getId());
    }

    @Test
    void findByEmail_NotOk() {
        userDao.save(createTestUser());
        Optional<User> actual = userDao.findByEmail(null);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        User testUser = userDao.save(createTestUser());
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(testUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_NotOk() {
        userDao.save(createTestUser());
        Optional<User> actual = userDao.findById(15L);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(roleUser));
        return user;
    }
}
