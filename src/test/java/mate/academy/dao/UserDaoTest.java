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
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(ROLE);
        roleDao.save(role);

        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Long id = 1L;
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "Method must return User object");
        Assertions.assertEquals(id, actual.getId(),
                "Expected object with id " + id + ", but was " + actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        String expected = userDao.save(user).getEmail();
        String actual = userDao.findByEmail(EMAIL).get().getEmail();
        Assertions.assertEquals(expected, actual,
                "Expected " + expected + ", but was " + actual);
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertEquals(Optional.empty(), actual,
                "Expected null value, but was " + actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Long id = 1L;
        Long actual = userDao.findById(id).get().getId();
        Assertions.assertEquals(id, actual,
                "Expected object with id " + id + ", but was " + actual);
    }

    @Test
    void findById_NotOk() {
        Long id = 1L;
        Optional<User> actual = userDao.findById(id);
        Assertions.assertEquals(Optional.empty(), actual,
                "Expected null value, but was " + actual);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
