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
    private static final String EMAIL = "bob@gmail.com";
    private static final String WRONG_EMAIL = "notbob@gmail.com";
    private static final String PASSWORD = "bobTheBest";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "Saved user can't be null");
        Assertions.assertEquals(actual.getId(), 1L,
                "Generated Id can't be null");
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent(), "Generated user fom email can't be null");
        Assertions.assertEquals(actual.get().getId(), 1L, "Parsed user has wrong id");
    }

    @Test
    void findByEmail_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(WRONG_EMAIL);
        Assertions.assertTrue(actual.isEmpty(), "Parsed user with wrong email, must be null");
    }
}
