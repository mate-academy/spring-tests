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
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserDao userDao;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        Role admin = new Role(Role.RoleName.ADMIN);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(admin);
        bob.setRoles(Set.of(admin));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void saveUserWithNullValue_NotOk() {
        User bob2 = new User();
        try {
            userDao.save(bob2);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + bob2, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(USER_EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("alice@gmail.com");
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        User actual = userDao.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_PASSWORD, actual.getPassword());
    }

    @Test
    void findById_NotOk() {
        userDao = new UserDaoImpl(null);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(1L));
    }
}
