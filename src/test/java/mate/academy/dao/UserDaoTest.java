package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(ROLE);
        roleDao.save(role);

        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Long expectedId = 1L;

        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedId, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User expected = userDao.save(user);
        Assertions.assertEquals(expected.getEmail(), userDao.findByEmail(EMAIL).get().getEmail());
    }

    @Test
    void findByEmail_NotOk() {
        Assertions.assertEquals(Optional.empty(), userDao.findByEmail(EMAIL));
    }

    @Test
    void findById_Ok() {
        User expected = userDao.save(user);
        Long id = 1L;
        Assertions.assertEquals(id, userDao.findById(id).get().getId());
    }

    @Test
    void findById_NotOk() {
        Assertions.assertEquals(Optional.empty(), userDao.findById(1L));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
