package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "12345678";
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static User user;
    private static Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
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

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        User actual = userDao.findByEmail(EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        User actual = userDao.findById(user.getId()).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findById_DataProcessingException() {
        try {
            userDao.findById(2L);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't get entity: + user by id 2L", e.getMessage());
        }
    }
}
