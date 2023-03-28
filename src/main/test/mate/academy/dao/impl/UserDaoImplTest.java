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

class UserDaoImplTest extends AbstractDaoTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private RoleDao roleDao;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        expectedUser = userDao.save(user);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        Assertions.assertEquals(expectedUser.getId(), 1L);
        Assertions.assertNotNull(expectedUser);
    }

    @Test
    void findByEmail_ok() {
        User user = userDao.findByEmail(EMAIL).get();
        Assertions.assertEquals(user.getEmail(), EMAIL);
    }

    @Test
    void findByEmail_wrongEmail_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(WRONG_EMAIL).get());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get());
    }

    @Test
    void findByEmail_emptyEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("").get());
    }

    @Test
    void findById_Ok() {
        User user = userDao.findById(1L).get();
        long actual = 1L;
        Assertions.assertEquals(user.getId(), actual);
    }

    @Test
    void findById_wrongId_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(-1L).get());
    }

    @Test
    void findById_nullId_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null).get());
    }
}