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
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        User actual = userDao.save(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_userIsNull_notOk() {
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            userDao.save(null);
        }, "DataProcessingException was expected");
        Assertions.assertEquals("Can't create entity: null", exception.getLocalizedMessage());
    }

    @Test
    void save_usersEmailIsNull_notOk() {
        User user = new User();
        user.setPassword("1111");
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            userDao.save(user);
        }, "DataProcessingException was expected");
        Assertions.assertEquals("Can't create entity: " + user, exception.getLocalizedMessage());
    }

    @Test
    void findByEmail_ok() {
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role save = roleDao.save(new Role(Role.RoleName.USER));
        Assertions.assertNotNull(save);
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        userDao.save(user);
        Assertions.assertNotNull(user);
        Optional<User> userOptional = userDao.findByEmail(user.getEmail());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
    }

    @Test
    void findById_ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        userDao.save(user);
        Optional<User> userOptional = userDao.findById(user.getId());
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void findById_idIsNull_notOk() {
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            userDao.findById(null);
        }, "DataProcessingException was expected");
        Assertions.assertEquals("Can't get entity: User by id null",
                exception.getLocalizedMessage());
    }
}
