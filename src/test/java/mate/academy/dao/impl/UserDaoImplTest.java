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
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123456789";
    private static final long BOB_ID = 1L;
    private static final String INVALID_EMAIL = "invalidEmail";
    private static final Long INVALID_ID = -2L;
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());

        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(BOB_ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BOB_ID, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(BOB_ID);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BOB_ID, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_invalidId_notOk() {
        Optional<User> actual = userDao.findById(INVALID_ID);
        Assertions.assertFalse(actual.isPresent());
    }
}
