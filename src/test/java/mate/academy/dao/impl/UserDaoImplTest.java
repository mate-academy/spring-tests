package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractClass {
    private static final String USER_EMAIL = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private User user;
    private Role role;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.USER))));
    }

    @Test
    void save_ToDb_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_EmailExist_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        User actualUser = actual.get();
        Assertions.assertEquals(USER_EMAIL, actualUser.getEmail());
    }

    @Test
    void findByEmail_EmailDoesNotExist_NotOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertTrue(actual.isEmpty());
    }
}
