package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

class UserDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private UserDao userDao;
    private String correctEmail;
    private String incorrectEmail;
    private String password;
    private Set<Role> roles;
    private Long identifier;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        correctEmail = "bchupika@mate.academy";
        incorrectEmail = "safasffsa@asfsfa";
        password = "12345678";
        roleDao.save(new Role(Role.RoleName.USER));
        roles = Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get());
        identifier = 1L;
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "User must not be null for: " + user);
        Assertions.assertEquals(identifier,actual.getId(),
                "Identifier must be: " + identifier + " but was: " + actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        userDao.save(user);
        User actual = userDao.findByEmail(correctEmail).get();
        Assertions.assertNotNull(actual, "User must be not null for: " + correctEmail);
    }

    @Test
    void findByEmail_NotOk() {
        User user = new User();
        user.setRoles(roles);
        user.setPassword(password);
        user.setEmail(correctEmail);
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(incorrectEmail);
        Assertions.assertTrue(actual.isEmpty(), "True is expected for email: " + incorrectEmail);
    }
}