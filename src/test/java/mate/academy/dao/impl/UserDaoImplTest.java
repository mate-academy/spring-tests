package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String ERROR = "ERROR";
    private static final String PASSWORD = "11111111";
    private static final Long ID = 1L;
    private static final Long WRONG_ID = 2L;
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(actual.get().getId(), ID);
        Assertions.assertEquals(actual.get().getEmail(), EMAIL);
        Assertions.assertEquals(actual.get().getPassword(), PASSWORD);
    }

    @Test
    void findByEmail_notOk() {
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userDao.findByEmail(ERROR).get();
        }, "NoSuchElementException expected");
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        User actual = userDao.findById(ID).get();
        Assertions.assertNotNull(actual);
    }

    @Test
    void findById_NotOk() {
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userDao.findById(WRONG_ID).get();
        }, "NoSuchElementException expected");
    }
}
