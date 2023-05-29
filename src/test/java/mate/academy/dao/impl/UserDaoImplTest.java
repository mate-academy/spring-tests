package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "123456";
    private static final String WRONG_EMAIL = "a.com";
    private UserDaoImpl userDao;
    private RoleDao roleDao;
    private User user;

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
        user = new User();
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), user.getId());
        Assertions.assertEquals(actual.getEmail(), user.getEmail());
    }

    @Test
    void save_userNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_ok() {
        User save = userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_wrongEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(WRONG_EMAIL);
        Assertions.assertFalse(actual.isPresent());
    }
}
