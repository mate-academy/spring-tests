package mate.academy.dao.impl;

import java.util.Optional;
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
    private static final String EMAIL = "ester.ua";
    private static final String PASSWORD = "987654321";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());

        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        user = userDao.save(this.user);
    }

    @Test
    void save_Ok() {
        Assertions.assertEquals(1L, user.getId());
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual);
        User actualUser = actual.get();
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
    }

    @Test
    void findByEmail_DataProcessingException() {
        userDao = new UserDaoImpl(null);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findByEmail(EMAIL));
    }
}
