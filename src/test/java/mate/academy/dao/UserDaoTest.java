package mate.academy.dao;

import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bobik@g.com";
    private static final String PASSWORD = "1234567890";
    private UserDao userDao;
    private RoleDao roleDao;
    private User actual;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = new Role(Role.RoleName.ADMIN);
        userRole = new Role(Role.RoleName.USER);
        roleDao.save(adminRole);
        roleDao.save(userRole);
        actual = userDao.save(getNewUser());
    }

    @Test
    void save_Ok() {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User userFindByEmail = userDao.findByEmail(EMAIL).get();
        Assertions.assertNotNull(userFindByEmail);
        Assertions.assertTrue(userFindByEmail.getEmail().equals(actual.getEmail()));
    }

    @Test
    void findByEmail_WrongEmail_NotOk() {
        Assertions.assertTrue(userDao.findByEmail(PASSWORD).isEmpty());
    }

    @Test
    void findById_Ok() {
        User userReq = userDao.findById(1L).get();
        Assertions.assertNotNull(userReq);
        Assertions.assertTrue(userReq.getId() == actual.getId());
    }

    @Test
    void findById_WrongId_NotOk() {
        Assertions.assertTrue(userDao.findById(100L).isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    private User getNewUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        return user;
    }
}
