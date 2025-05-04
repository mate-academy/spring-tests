package mate.academy.dao;

import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        RoleName roleNameUser = RoleName.USER;
        userRole = new Role(roleNameUser);
        user = new User();
        user.setEmail("vvv@i.ua");
        user.setPassword("12341234");
        user.setRoles(Set.of(userRole));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        userRole = roleDao.save(userRole);
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(userRole.getRoleName(),
                actual.getRoles().stream()
                        .filter(r -> r.getRoleName().equals(RoleName.USER))
                        .findAny()
                        .get()
                        .getRoleName());
    }

    @Test
    void findByEmail_Ok() {
        userRole = roleDao.save(userRole);
        user = userDao.save(user);
        User actual = userDao.findByEmail("vvv@i.ua").get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("12341234", actual.getPassword());
    }

    @Test
    void findById() {
        userRole = roleDao.save(userRole);
        user = userDao.save(user);
        User actual = userDao.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("12341234", actual.getPassword());
    }
}
