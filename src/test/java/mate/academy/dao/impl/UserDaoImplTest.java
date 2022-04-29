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
import test.java.mate.academy.AbstractTest;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId(), "Id for first saved user should be 1");
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent(),
                "For existing user with valid email Optional should no be empty");
        Assertions.assertEquals(1L, actual.get().getId());
    }

    @Test
    void findByEmail_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("not email");
        Assertions.assertTrue(actual.isEmpty(), "For non existing email Optional should be empty");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
