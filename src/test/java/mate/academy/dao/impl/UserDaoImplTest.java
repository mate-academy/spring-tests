package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private String email = "bob@i.ua";
    private String password = "1234";
    private UserDao userDao;
    private RoleDao roleDao;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        bob = new User();
        bob.setEmail(email);
        bob.setPassword(password);
        bob.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        User actual = userDao.findByEmail(email).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        User actual = userDao.findById(bob.getId()).get();
        Assertions.assertEquals(bob.getId(), actual.getId());
        Assertions.assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_NoSuchElementException_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("wrongEmail").get());
    }

    @Test
    void findById_DataProcessingException_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null).get());
    }
}
