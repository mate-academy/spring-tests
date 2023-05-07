package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractDaoTest {
    private static final String EMAIL = "den@gmail.com";
    private static final String PASS = "77777777";
    private UserDao userDao;
    private RoleDao roleDao;
    private User den;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        den = new User();
        den.setEmail(EMAIL);
        den.setPassword(PASS);
        den.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(den);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(PASS, actual.getPassword());

    }

    @Test
    void findByEmail_Ok() {
        userDao.save(den);
        User actual = userDao.findByEmail(EMAIL).orElseThrow(NoSuchElementException::new);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(PASS, actual.getPassword());
    }

    @Test
    void findByEmail_NoSuchElementException() {
        Exception exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                    userDao.findByEmail(EMAIL).get();
                }, "NoSuchElementException was expected.");
    }

    @Test
    void findById_Ok() {
        userDao.save(den);
        User actual = userDao.findById(1L).orElseThrow(NoSuchElementException::new);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASS, actual.getPassword());
    }

    @Test
    void findByEmail_noSuchElementException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userDao.findByEmail(EMAIL).get();
        }, "NoSuchElementException was expected.");
    }
}
