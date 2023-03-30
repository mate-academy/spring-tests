package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static Role role;
    private static User user;
    private RoleDao roleDao;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        roleDao.save(role);
        User actual = userDao.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findByEmail_Ok() {
        roleDao.save(role);
        userDao.save(user);
        Long expectedId = 1L;

        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(expectedId, actual.get().getId());
    }

    @Test
    void findById_Ok() {
        roleDao.save(role);
        userDao.save(user);
        Long id = 1L;

        Optional<User> actual = userDao.findById(id);
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
