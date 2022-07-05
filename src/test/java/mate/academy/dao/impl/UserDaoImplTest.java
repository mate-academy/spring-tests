package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "email@com.ua";
    private static final String PASSWORD = "123456789";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullInput_notOk() {
        try {
            userDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_wrongEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("qwerw");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_ok() {
        User savedUser = userDao.save(user);
        Optional<User> actual = userDao.findById(savedUser.getId());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_wrongId_notOk() {
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
