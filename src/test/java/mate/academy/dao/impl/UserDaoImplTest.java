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

class UserDaoImplTest extends AbstractDaoTest {
    public static final String VALID_EMAIL = "email@test.test";
    public static final String WRONG_EMAIL = "wrong@test.test";
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
    void findByEmail_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        User expected = new User();
        expected.setEmail(VALID_EMAIL);
        expected.setRoles(Set.of(role));
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail(),
                String.format("Method should return user with email: %s, but returned: %s",
                        VALID_EMAIL, actual));
    }

    @Test
    void findByNonExistingEmail_notOk() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        User expected = new User();
        expected.setEmail(VALID_EMAIL);
        expected.setRoles(Set.of(role));
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail(WRONG_EMAIL);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional if email is missing in db,"
                        + " but returned: %s with email : %s", actual, WRONG_EMAIL));
    }
}
