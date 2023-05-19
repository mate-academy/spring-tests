package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(userRole));
        roleDao.save(userRole);
    }

    @Test
    void userDao_save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void userDao_findByEmail_Ok() {
        User expected = userDao.save(user);
        User actual = userDao.findByEmail(user.getEmail()).get();
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void userDao_findById_Ok() {
        User expected = userDao.save(user);
        User actual = userDao.findById(user.getId()).get();
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void userDao_saveNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class, ()
                -> userDao.save(null));
    }

    @Test
    void userDao_findByWrongEmail_notOk() {
        userDao.save(user);
        Assertions.assertEquals(Optional.empty(),
                userDao.findByEmail("WrongEmail"));
    }

    @Test
    void userDao_saveNotUniqueUser_notOk() {
        userDao.save(user);
        User secondUser = user;
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(secondUser));
    }
}
