package mate.academy.dao.impl;

import java.util.NoSuchElementException;
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
    private RoleDao roleDao;
    private String email;
    private User test;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        test = new User();
        test.setEmail(email);
        test.setPassword("1234");
        test.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User saveToDb = userDao.save(test);
        Assertions.assertNotNull(saveToDb);
        Assertions.assertEquals(1L,saveToDb.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(test);
        User findByEmail = userDao.findByEmail(email).get();
        Assertions.assertEquals(findByEmail.getEmail(),email);
    }

    @Test
    void findByEmail_WrongEmail_NotOk() {
        userDao.save(test);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("test").get());
    }

    @Test
    void findByEmail_EmptyEmail_NotOk() {
        userDao.save(test);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("").get());
    }

    @Test
    void findByEmail_NullEmail_NotOk() {
        userDao.save(test);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get());
    }

    @Test
    void findById_Ok() {
        userDao.save(test);
        User findByEmail = userDao.findById(1L).get();
        Assertions.assertEquals(findByEmail.getId(),1L);
    }

    @Test
    void findById_WrongId_NotOk() {
        userDao.save(test);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(2L).get());
    }

    @Test
    void findById_NullId_NotOk() {
        userDao.save(test);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null).get());
    }
}
