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
    private static final String TEST_EMAIL = "test@test.test";
    private Role testRole;
    private User user;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        testRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword("1234");
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "User shouldn't be null");
        Assertions.assertEquals(1L, actual.getId(),
                String.format("User id after save should be 1, but was: %d", actual.getId()));
    }

    @Test
    void findByEmail_Ok() {
        user.setRoles(Set.of(testRole));
        roleDao.save(testRole);
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertTrue(actual.isPresent(),
                String.format("Should return user by email: %s, but user was empty", TEST_EMAIL));
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                String.format("Should return user by email: %s, but user was empty", TEST_EMAIL));
    }

    @Test
    void findByEmail_emailIsNotExist_notOk() {
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for email: %s, "
                        + "but was: %s", TEST_EMAIL, actual));
    }

    @Test
    void findByEmail_emailIsNull_notOk() {
        Optional<User> actual = userDao.findByEmail(null);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for null email, but was: ", actual));
    }

    @Test
    void findById_Ok() {
        user.setRoles(Set.of(testRole));
        roleDao.save(testRole);
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        Assertions.assertTrue(actual.isPresent(),
                String.format("Should return user by id: %d, but user was empty", user.getId()));
        Assertions.assertEquals(user.getId(), actual.get().getId(),
                String.format("Should return user by id: %d, but user was empty", user.getId()));
    }

    @Test
    void findById_userIdIsNotExist_notOk() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for id: %d, "
                        + "but was: %s", 1L, actual));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
