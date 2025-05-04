package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final Long USER_ID = 1L;
    private static final Long USER_ID_NOT_EXIST_IN_DB = 2L;
    private static final String EMAIL = "right@mail.com";
    private UserDao userDao;
    private User expected;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        expected = new User();
        expected.setEmail(EMAIL);
        expected.setPassword("12345678");
        expected.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void findById_ok() {
        userDao.save(expected);

        User actual = userDao.findById(USER_ID).get();

        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void findById_idNotExist_notOk() {
        assertThrows(RuntimeException.class, () ->
                userDao.findById(USER_ID_NOT_EXIST_IN_DB).get()
        );
    }

    @Test
    void findById_idNull_notOk() {
        assertThrows(RuntimeException.class,
                () -> userDao.findById(null).get()
        );
    }

    @Test
    void findByEmail_ok() {
        userDao.save(expected);

        User actual = userDao.findByEmail(EMAIL).get();

        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_emailNotExistInDb_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("emailNotExistInDb@email.com").get());
    }

    @Test
    void findByEmail_emailNull_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get());
    }
}
