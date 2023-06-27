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
    private static final String EMAIL = "testmail@i.ua";
    private static final String PASSWORD = "12345";
    private static final long ID = 1L;
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
        Assertions.assertEquals(ID, actual.getId());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("falded@email");
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ID, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }
    @Test
    void findByID_NotOk() {
        Optional<User> actual = userDao.findById(8L);
        Assertions.assertFalse(actual.isPresent());
    }

        @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(ID);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ID, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }
}