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
    public static final String EMAIL = "bob@i.ua";
    public static final String UNVALID_EMAIL = "falseuser@i.ua";
    public static final String PASSWORD = "1234";
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
        Role role = new Role(Role.RoleName.USER);
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
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void getRoleByName_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void getRoleByName_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(UNVALID_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }
}
