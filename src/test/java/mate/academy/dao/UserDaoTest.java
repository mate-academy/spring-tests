package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob123@gmail.com";
    private static final String PASSWORD = "123";
    private static UserDao userDao;
    private static RoleDao roleDao;
    private final User bob = new User();

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role savedRole = roleDao.save(role);
        bob.setPassword(PASSWORD);
        bob.setEmail(EMAIL);
        bob.setRoles(Set.of(savedRole));
    }

    @Test
    void save_Ok() {
        User savedUser = userDao.save(bob);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(1L, savedUser.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        User userFromDb = userDao.findByEmail(EMAIL).get();
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(1L, userFromDb.getId());
    }

    @Test
    void findByEmail_NotOk() {
        String notExistUserEmail = "alice@gmail.com";
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(notExistUserEmail).get());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        User userFromDb = userDao.findById(1L).get();
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(1L, userFromDb.getId());
    }

    @Test
    void findById_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userDao.findById(1L).get());
    }
}
