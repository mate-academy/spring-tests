package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

class UserDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private UserDao userDao;
    private static User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @BeforeAll
    static void beforeAll() {
        String correctEmail = "bchupika@mate.academy";
        String password = "12345678";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
    }

    @Test
    void save_Ok() {
        Long identifier = 1L;
        Role userRole = roleDao.save(new Role(Role.RoleName.USER));
        user.setRoles(Set.of(userRole));
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "User must not be null for: " + user);
        Assertions.assertEquals(identifier,actual.getId(),
                "Identifier must be: " + identifier + " but was: " + actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        String correctEmail = "bchupika@mate.academy";
        Role userRole = roleDao.save(new Role(Role.RoleName.USER));
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        User actual = userDao.findByEmail(correctEmail).get();
        Assertions.assertNotNull(actual, "User must be not null for: " + correctEmail);
    }

    @Test
    void findByEmail_nonExistentUser_NotOk() {
        String incorrectEmail = "asdjhfg---...@fmail.com";
        Role userRole = roleDao.save(new Role(Role.RoleName.USER));
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(incorrectEmail);
        Assertions.assertTrue(actual.isEmpty(), "True is expected for email: " + incorrectEmail);
    }
}
