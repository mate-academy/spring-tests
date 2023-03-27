package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private User userAlice;
    private User userBob;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = getSessionFactory();
        roleDao = new RoleDaoImpl(sessionFactory);
        Role adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(adminRole);
        userAlice = new User();
        userAlice.setEmail("alice@gmail.com");
        userAlice.setPassword("qwerty");
        userAlice.setRoles(Set.of(adminRole));
        userBob = new User();
        userBob.setEmail("bob@gmail.com");
        userBob.setPassword("1234");
        userDao = new UserDaoImpl(sessionFactory);
        userDao.save(userAlice);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(userBob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(userAlice.getEmail());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(userAlice.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_userNotFound_notOk() {
        Optional<User> actual = userDao.findByEmail(userBob.getEmail());
        Assertions.assertThrows(NoSuchElementException.class, actual::get);
    }

    @Test
    void findById_Ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(userAlice.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_userNotFound_notOk() {
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertThrows(NoSuchElementException.class, actual::get);
    }
}
