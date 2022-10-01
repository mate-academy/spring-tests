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
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        Role userRole = new Role(Role.RoleName.USER);
        Role role = roleDao.save(userRole);
        user.setRoles(Set.of(role));
        userDao.save(user);

        Optional<User> actual = userDao.findByEmail(USER_LOGIN);
        Assertions.assertTrue(actual.isPresent());
        User actualUser = actual.get();
        Assertions.assertEquals(USER_LOGIN, actualUser.getEmail());
    }

    @Test
    void findByEmail_Not_Ok_DataProcessingException() {
        userDao = new UserDaoImpl(null);

        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findByEmail(USER_LOGIN));
    }
}
