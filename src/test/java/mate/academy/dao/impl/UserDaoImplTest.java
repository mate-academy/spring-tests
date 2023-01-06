package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;
    private User bob;
    private Role user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new Role(Role.RoleName.USER);
        roleDao.save(user);
        bob = new User(EMAIL, PASSWORD, Set.of(user));
    }

    @Test
    void save_correctData_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_correctData_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(EMAIL).get();
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void findByEmail_incorrectData_NotOk() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(EMAIL.toUpperCase());
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_correctData_Ok() {
        userDao.save(bob);
        User actual = userDao.findById(1L).get();
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void findById_incorrectId_NotOk() {
        Optional<User> actual = userDao.findById(10L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_inputNull_DataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class, () -> {
            userDao.findById(null);
        });
    }
}
