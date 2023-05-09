package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
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
    void findByEmail_Ok() {
        User user = userDao.findByEmail(EMAIL).get();
        Assertions.assertEquals(user.getEmail(), EMAIL);
    }

    @Test
    void findByEmail_wrongEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("wrong@email").get());
    }

    @Test
    void findByEmail_EmailIsNull_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get());
    }

    @Test
    void findByEmail_EmailIsEmpty_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("").get());
    }

    @Test
    void findById_Ok() {
        User user = userDao.findById(1L).get();
        Assertions.assertEquals(user.getId(), 1L);
    }

    @Test
    void findById_wrongId_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(3L).get());
    }

    @Test
    void findById_IdIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null).get());
    }
}
