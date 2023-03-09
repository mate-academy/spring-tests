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
    private static final Role TEST_ROLE = new Role(Role.RoleName.USER);
    private User user;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword("1234");
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
        user.setRoles(Set.of(TEST_ROLE));
        roleDao.save(TEST_ROLE);
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
        user.setRoles(Set.of(TEST_ROLE));
        roleDao.save(TEST_ROLE);
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent(),
                String.format("Should return user by id: %d, but user was empty", 1L));
        Assertions.assertEquals(1L, actual.get().getId(),
                String.format("Should return user by id: %d, but user was empty", 1L));
    }

    @Test
    void findByEmail_userIdIsNotExist_notOk() {
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
