package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.util.UserTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = getSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
        roleDao = new RoleDaoImpl(sessionFactory);
    }

    @Test
    void findByEmail_Ok() {
        User expected = UserTestUtil.getUserBob();
        roleDao.save(expected.getRoles().stream().findFirst().get());
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail(expected.getEmail());
        Assertions.assertTrue(actual.isPresent());
        checkUsers(expected, actual.get());
        Assertions.assertEquals(UserTestUtil.getListOfStringRoles(expected),
                UserTestUtil.getListOfStringRoles(actual.get()));
    }

    @Test
    void findByEmailNoInDataBase_NotOk() {
        Optional<User> user = userDao
                .findByEmail(UserTestUtil.INCORRECT_EMAIL);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void findById_Ok() {
        User expected = UserTestUtil.getUserBob();
        roleDao.save(expected.getRoles().stream().findFirst().get());
        userDao.save(expected);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        User actualUser = actual.get();
        checkUsers(expected, actualUser);
    }

    @Test
    void findById_NotOk() {
        Optional<User> noPresentedUser = userDao.findById(777L);
        Assertions.assertTrue(noPresentedUser.isEmpty());
    }

    @Test
    void save_Ok() {
        User expected = UserTestUtil.getUserBob();
        roleDao.save(expected.getRoles().stream().findFirst().get());
        User actual = userDao.save(expected);
        checkUsers(expected, actual);
        Assertions.assertEquals(UserTestUtil.getListOfStringRoles(expected),
                UserTestUtil.getListOfStringRoles(actual));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    private void checkUsers(User expected, User actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }
}
