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
    private static final String EMAIl = "bob@mail.com";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private User user;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());

        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIl);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void save_NotOk_DataProcessingException() {
        User bob = null;
        try {
            userDao.save(bob);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + bob, e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIl);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIl, actual.get().getEmail());
    }

    @Test
    void findByEmail_EmptyUser() {
        Optional<User> actual = userDao.findByEmail("bob@");
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }
}
