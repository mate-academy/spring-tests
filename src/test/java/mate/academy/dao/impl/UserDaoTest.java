package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, User.class};
    }

    @Test
    void saveUser_Ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");

        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");

        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        user.setRoles(Set.of(role));
        userDao.save(user);

        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals("bob@i.ua", actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");

        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        user.setRoles(Set.of(role));
        userDao.save(user);

        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_invalidId_notOk() {
        Optional<User> userOptional = userDao.findById(1L);
        Assertions.assertTrue(userOptional.isEmpty());
    }
}
