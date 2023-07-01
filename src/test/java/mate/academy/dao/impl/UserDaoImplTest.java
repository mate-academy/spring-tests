package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoImplTest extends AbstractTest {
    private static final String VALID_EMAIL = "test@mail.net";
    private static final String VALID_PASSWORD = "test1234";
    private static final Long VALID_USER_ID = 1L;
    private static final String INVALID_EMAIL = "test12@mail.net";
    private static final Long INVALID_USER_ID = 2L;
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_USER_ID, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        System.out.print(user.getId());
        Optional<User> actual = userDao.findByEmail(VALID_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(VALID_USER_ID, actual.get().getId());
        Assertions.assertEquals(VALID_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(VALID_PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findById_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(VALID_USER_ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(VALID_USER_ID, actual.get().getId());
    }

    @Test
    void findById_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(INVALID_USER_ID);
        Assertions.assertFalse(actual.isPresent());
    }
}
