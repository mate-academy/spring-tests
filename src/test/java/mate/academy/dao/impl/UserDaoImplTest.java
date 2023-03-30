package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String TEST_EMAIL = "email@is.test";
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void findByEmail_Ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setRoles(Set.of(role));
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                "Actual user should have email: " + TEST_EMAIL
                        + " but was: " + actual.get().getEmail());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertFalse(actual.isPresent(),
                "Method should return empty optional for email: " + TEST_EMAIL
                        + " but was: " + actual);
    }
}
