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
    private static UserDao userDao;
    private static RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = createRawUser(role);
    }

    @Test
    void saveUser_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(user, actual);
    }

    @Test
    void saveUser_existingUser_notOk() {
        userDao.save(user);
        try {
            userDao.save(user);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void saveUser_nullEmail_notOk() {
        user.setEmail(null);
        try {
            userDao.save(user);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("user1@gmail.com");
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findByEmail_notExistingEmail_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("user2@gmail.com");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_emptyEmail_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(null);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_notExistingId_notOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createRawUser(Role role) {
        User user = new User();
        user.setEmail("user1@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(role));
        return user;
    }
}
